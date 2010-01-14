/*
 * Copyright (c) 2008-2010, Stephen Colebourne & Michael Nascimento Santos
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  * Neither the name of JSR-310 nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package javax.time.period;

import java.io.Serializable;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map.Entry;

import javax.time.CalendricalException;
import javax.time.Duration;
import javax.time.MathUtils;
import javax.time.calendar.PeriodUnit;

/**
 * A period of time measured using a number of different units,
 * such as '3 Months, 4 Days and 7 Hours'.
 * <p>
 * <code>PeriodFields</code> is an immutable period that stores an amount of human-scale
 * time for a number of units. For example, humans typically measure periods of time
 * in units of years, months, days, hours, minutes and seconds. These concepts are
 * defined by instances of {@link PeriodUnit} in the chronology classes. This class
 * allows an amount to be specified for a number of the units, such as '3 Days and 65 Seconds'.
 * <p>
 * Basic mathematical operations are provided - plus(), minus(), multipliedBy(),
 * dividedBy() and negated(), all of which return a new instance
 * <p>
 * A value of zero can also be stored for any unit. This means that a
 * period of zero hours is not equal to a period of zero minutes.
 * However, an empty instance constant exists to represent zero irrespective of unit.
 * The {@link #withZeroesRemoved()} method removes zero values.
 * <p>
 * <code>PeriodFields</code> can store units of any kind which makes it usable with
 * any calendar system.
 * <p>
 * PeriodFields is immutable and thread-safe.
 *
 * @author Stephen Colebourne
 */
public final class PeriodFields
        implements PeriodProvider, Iterable<PeriodField>, Serializable {

    /**
     * A constant for a period of zero.
     * This constant is independent of any unit.
     */
    public static final PeriodFields ZERO = new PeriodFields(new TreeMap<PeriodUnit, PeriodField>());
    /**
     * The serialization version.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The map of periods.
     */
    private final TreeMap<PeriodUnit, PeriodField> unitFieldMap;

    //-----------------------------------------------------------------------
    /**
     * Obtains a <code>PeriodFields</code> from an amount and unit.
     * <p>
     * The parameters represent the two parts of a phrase like '6 Days'.
     *
     * @param amount  the amount of create with, may be negative
     * @param unit  the period unit, not null
     * @return the <code>PeriodFields</code> instance, never null
     * @throws NullPointerException if the period unit is null
     */
    public static PeriodFields of(long amount, PeriodUnit unit) {
        checkNotNull(unit, "PeriodUnit must not be null");
        TreeMap<PeriodUnit, PeriodField> internalMap = createMap();
        internalMap.put(unit, PeriodField.of(amount, unit));
        return create(internalMap);
    }

    /**
     * Obtains a <code>PeriodFields</code> from a set of unit-amount pairs.
     * <p>
     * The amount to store for each unit is obtained by calling {@link Number#longValue()}.
     * This will lose any decimal places for instances of <code>Double</code> and <code>Float</code>.
     * It may also silently lose precision for instances of <code>BigInteger</code> or <code>BigDecimal</code>.
     *
     * @param unitAmountMap  a map of periods that will be used to create this
     *  period, not updated by this method, not null, contains no nulls
     * @return the <code>PeriodFields</code> instance, never null
     * @throws NullPointerException if the map is null or contains nulls
     */
    public static PeriodFields of(Map<PeriodUnit, ? extends Number> unitAmountMap) {
        checkNotNull(unitAmountMap, "Map must not be null");
        // don't use contains() as tree map and others can throw NPE
        TreeMap<PeriodUnit, PeriodField> internalMap = createMap();
        for (Entry<PeriodUnit, ? extends Number> entry : unitAmountMap.entrySet()) {
            PeriodUnit unit = entry.getKey();
            Number amount = entry.getValue();
            checkNotNull(unit, "Null keys are not permitted in unit-amount map");
            checkNotNull(amount, "Null amounts are not permitted in unit-amount map");
            internalMap.put(unit, PeriodField.of(amount.longValue(), unit));
        }
        return create(internalMap);
    }

    //-----------------------------------------------------------------------
    /**
     * Obtains a <code>PeriodFields</code> from a <code>PeriodProvider</code>.
     * <p>
     * This factory returns an instance with all the unit-amount pairs from the provider.
     *
     * @param periodProvider  the provider to create from, not null
     * @return the <code>PeriodFields</code> instance, never null
     * @throws NullPointerException if the period provider is null
     */
    public static PeriodFields from(PeriodProvider periodProvider) {
        checkNotNull(periodProvider, "PeriodProvider must not be null");
        if (periodProvider instanceof PeriodFields) {
            return (PeriodFields) periodProvider;
        }
        TreeMap<PeriodUnit, PeriodField> map = createMap();
        if (periodProvider instanceof PeriodField) {
            PeriodField providedField = (PeriodField) periodProvider;
            map.put(providedField.getUnit(), providedField);
        } else {
            for (PeriodUnit unit : periodProvider.periodRules()) {
                long amount = periodProvider.periodAmount(unit);
                map.put(unit, PeriodField.of(amount, unit));
            }
        }
        return create(map);
    }

    /**
     * Obtains a <code>PeriodFields</code> by totalling the amounts in a list of
     * <code>PeriodProvider</code> instances.
     * <p>
     * This method returns a period with all the unit-amount pairs from the providers
     * totalled. Thus a period of '2 Months and 5 Days' combined with a period of
     * '7 Days and 21 Hours' will yield a result of '2 Months, 12 Days and 21 Hours'.
     *
     * @param periodProviders  the providers to total, not null
     * @return the <code>PeriodFields</code> instance, never null
     * @throws NullPointerException if the period provider is null
     */
    public static PeriodFields total(PeriodProvider... periodProviders) {
        checkNotNull(periodProviders, "PeriodProvider[] must not be null");
        if (periodProviders.length == 1 && periodProviders[0] instanceof PeriodFields) {
            return (PeriodFields) periodProviders[0];
        }
        TreeMap<PeriodUnit, PeriodField> map = createMap();
        for (PeriodProvider periodProvider : periodProviders) {
            if (periodProvider instanceof PeriodField) {
                PeriodField providedField = (PeriodField) periodProvider;
                PeriodField field = map.get(providedField.getUnit());
                field = (field != null ? field.plus(providedField.getAmount()) : providedField);
                map.put(providedField.getUnit(), field);
            } else {
                for (PeriodUnit unit : periodProvider.periodRules()) {
                    long amount = periodProvider.periodAmount(unit);
                    PeriodField field = map.get(unit);
                    field = (field != null ? field.plus(amount) : PeriodField.of(amount, unit));
                    map.put(unit, field);
                }
            }
        }
        return create(map);
    }

    //-----------------------------------------------------------------------
    /**
     * Creates a new empty map.
     *
     * @return ordered representation of internal map
     */
    private static TreeMap<PeriodUnit, PeriodField> createMap() {
        return new TreeMap<PeriodUnit, PeriodField>(Collections.reverseOrder());
    }

    /**
     * Internal factory to create an instance using a pre-built map.
     * The map must not be used by the calling code after calling the constructor.
     *
     * @param periodMap  the unit-amount map, not null, assigned not cloned
     * @return the created period, never null
     */
    private static PeriodFields create(TreeMap<PeriodUnit, PeriodField> periodMap) {
        if (periodMap.isEmpty()) {
            return ZERO;
        }
        return new PeriodFields(periodMap);
    }

    /**
     * Validates that the input value is not null.
     *
     * @param object  the object to check
     * @param errorMessage  the error to throw
     * @throws NullPointerException if the object is null
     */
    static void checkNotNull(Object object, String errorMessage) {
        if (object == null) {
            throw new NullPointerException(errorMessage);
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Constructs an instance using a pre-built map.
     * The map must not be used by the calling code after calling the constructor.
     *
     * @param periodMap  the map of periods to represent, not null and safe to assign
     */
    private PeriodFields(TreeMap<PeriodUnit, PeriodField> periodMap) {
        this.unitFieldMap = periodMap;
    }

    /**
     * Resolves singletons.
     *
     * @return the resolved instance
     */
    private Object readResolve() {
        if (unitFieldMap.size() == 0) {
            return ZERO;
        }
        return this;
    }

    //-----------------------------------------------------------------------
//    /**
//     * Gets the complete set of fields.
//     *
//     * @return the period unit as an unmodifiable set, never null
//     */
//    public PeriodField[] periodFields() {
//        return unitFieldMap.values().toArray(new PeriodField[unitFieldMap.size()]);
//    }
    /**
     * Gets the complete set of units which have amounts stored.
     *
     * @return the period unit as an unmodifiable set, never null
     */
    public Set<PeriodUnit> periodRules() {
        return Collections.unmodifiableSet(unitFieldMap.keySet());
    }

    /**
     * Gets the amount stored for the specified unit.
     * <p>
     * Zero is returned if no amount is stored for the unit.
     *
     * @param unit  the unit to get, not null
     * @return the amount of time stored in this period for the unit
     */
    public long periodAmount(PeriodUnit unit) {
        PeriodFields.checkNotNull(unit, "PeriodRule must not be null");
        PeriodField field = unitFieldMap.get(unit);
        return (field != null ? field.getAmount() : 0);
    }

    //-----------------------------------------------------------------------
    /**
     * Checks if this period is zero-length.
     * <p>
     * This checks whether all the amounts in this period are zero.
     *
     * @return true if this period is zero-length
     */
    public boolean isZero() {
        for (PeriodField field : unitFieldMap.values()) {
            if (field.isZero() == false) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if this period is fully positive, including zero.
     * <p>
     * This checks whether all the amounts in this period are positive,
     * defined as greater than or equal to zero.
     *
     * @return true if this period is fully positive
     */
    public boolean isPositive() {
        for (PeriodField field : unitFieldMap.values()) {
            if (field.isNegative()) {
                return false;
            }
        }
        return true;
    }

    //-----------------------------------------------------------------------
    /**
     * Returns the size of the set of units in this period.
     * <p>
     * This returns the number of different units that are stored.
     *
     * @return number of unit-amount pairs
     */
    public int size() {
        return unitFieldMap.size();
    }

    /**
     * Iterates through all the single-unit periods in this period.
     * <p>
     * This method fulfills the {@link Iterable} interface and allows looping
     * around the contained single-unit periods using the for-each loop.
     *
     * @return an iterator over the single-unit periods in this period, never null
     */
    public Iterator<PeriodField> iterator() {
        return unitFieldMap.values().iterator();
    }

    /**
     * Checks whether this period contains an amount for the unit.
     *
     * @param unit  the unit to query, null returns false
     * @return true if the map contains an amount for the unit
     */
    public boolean contains(PeriodUnit unit) {
        return unitFieldMap.containsKey(unit);
    }

    //-----------------------------------------------------------------------
    /**
     * Gets the period for the specified unit.
     * <p>
     * This method allows the period to be queried by unit, like a map.
     * If the unit is not found then <code>null</code> is returned.
     *
     * @param unit  the unit to query, not null
     * @return the period, null if no period stored for the unit
     */
    public PeriodField get(PeriodUnit unit) {
        checkNotNull(unit, "PeriodRule must not be null");
        return unitFieldMap.get(unit);
    }

    /**
     * Gets the amount of this period for the specified unit.
     * <p>
     * This method allows the amount to be queried by unit, like a map.
     * If the unit is not found then an exception is thrown.
     *
     * @param unit  the unit to query, not null
     * @return the period amount
     * @throws CalendricalException if there is no amount for the unit
     */
    public long getAmount(PeriodUnit unit) {
        PeriodField field = get(unit);
        if (field == null) {
            throw new CalendricalException("No amount for unit: " + unit);
        }
        return field.getAmount();
    }

    /**
     * Gets the amount of this period for the specified unit converted
     * to an <code>int</code>.
     * <p>
     * This method allows the amount to be queried by unit, like a map.
     * If the unit is not found then an exception is thrown.
     *
     * @param unit  the unit to query, not null
     * @return the period amount
     * @throws CalendricalException if there is no amount for the unit
     * @throws ArithmeticException if the amount is too large to be returned in an int
     */
    public int getAmountInt(PeriodUnit unit) {
        return MathUtils.safeToInt(getAmount(unit));
    }

//    //-----------------------------------------------------------------------
//    /**
//     * Gets the amount of the period for the specified unit, returning
//     * the default value if this period does have an amount for the unit.
//     *
//     * @param unit  the unit to query, not null
//     * @param defaultValue  the default value to return if the unit is not present
//     * @return the period amount
//     * @throws NullPointerException if the period unit is null
//     */
//    public long get(PeriodRule unit, long defaultValue) {
//        checkNotNull(unit, "PeriodRule must not be null");
//        Long amount = unitAmountMap.get(unit);
//        return amount == null ? defaultValue : amount;
//    }
//
//    /**
//     * Gets the amount of the period for the specified unit, returning
//     * the default value if this period does have an amount for the unit.
//     * <p>
//     * The amount is safely converted to an <code>int</code>.
//     *
//     * @param unit  the unit to query, not null
//     * @param defaultValue  the default value to return if the unit is not present
//     * @return the period amount
//     * @throws NullPointerException if the period unit is null
//     * @throws ArithmeticException if the amount is too large to be returned in an int
//     */
//    public int getInt(PeriodRule unit, int defaultValue) {
//        return MathUtils.safeToInt(get(unit, defaultValue));
//    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this period with all zero amounts removed.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @return a new period with no zero amounts, never null
     */
    public PeriodFields withZeroesRemoved() {
        if (isZero()) {
            return ZERO;
        }
        TreeMap<PeriodUnit, PeriodField> copy = clonedMap();
        for (Iterator<PeriodField> it = copy.values().iterator(); it.hasNext(); ) {
            if (it.next().isZero()) {
                it.remove();
            }
        }
        return create(copy);
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this period with the specified amount for the unit.
     * <p>
     * If this period already contains an amount for the unit then the amount
     * is replaced. Otherwise, the unit-amount pair is added.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param amount  the amount to store in terms of the unit, may be negative
     * @param unit  the unit to store not null
     * @return a new period with the specified amount and unit, never null
     */
    public PeriodFields with(long amount, PeriodUnit unit) {
        PeriodField existing = get(unit);
        if (existing != null && existing.getAmount() == amount) {
            return this;
        }
        TreeMap<PeriodUnit, PeriodField> copy = clonedMap();
        copy.put(unit, PeriodField.of(amount, unit));
        return create(copy);
    }

//    /**
//     * Returns a copy of this period with the amounts from the specified map added.
//     * <p>
//     * If this instance already has an amount for any unit then the value is replaced.
//     * Otherwise the value is added.
//     * <p>
//     * This instance is immutable and unaffected by this method call.
//     *
//     * @param unitAmountMap  the new map of fields, not null
//     * @return a new updated period instance, never null
//     * @throws NullPointerException if the map contains null keys or values
//     */
//    public PeriodFields with(Map<PeriodRule, Long> unitAmountMap) {
//        checkNotNull(unitAmountMap, "The field-value map must not be null");
//        if (unitAmountMap.isEmpty()) {
//            return this;
//        }
//        // don't use contains() as tree map and others can throw NPE
//        TreeMap<PeriodRule, Long> clonedMap = clonedMap();
//        for (Entry<PeriodRule, Long> entry : unitAmountMap.entrySet()) {
//            PeriodRule unit = entry.getKey();
//            Long value = entry.getValue();
//            checkNotNull(unit, "Null keys are not permitted in field-value map");
//            checkNotNull(value, "Null values are not permitted in field-value map");
//            clonedMap.put(unit, value);
//        }
//        return new PeriodFields(clonedMap);
//    }

    /**
     * Returns a copy of this period with the specified values altered.
     * <p>
     * This method operates on each unit in the input in turn.
     * If this period already contains an amount for the unit then the amount
     * is replaced. Otherwise, the unit-amount pair is added.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param period  the period to store, not null
     * @return a new period with the specified period set, never null
     */
    public PeriodFields with(PeriodFields period) {
        if (this == ZERO) {
            return period;
        }
        if (period == ZERO) {
            return this;
        }
        TreeMap<PeriodUnit, PeriodField> copy = clonedMap();
        copy.putAll(period.unitFieldMap);
        return create(copy);
    }

    /**
     * Returns a copy of this period with the specified unit removed.
     * <p>
     * If this period already contains an amount for the unit then the amount
     * is removed. Otherwise, no action occurs.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param unit  the unit to remove, not null
     * @return a new period with the unit removed, never null
     */
    public PeriodFields withRuleRemoved(PeriodUnit unit) {
        checkNotNull(unit, "PeriodRule must not be null");
        if (unitFieldMap.containsKey(unit) == false) {
            return this;
        }
        TreeMap<PeriodUnit, PeriodField> copy = clonedMap();
        copy.remove(unit);
        return create(copy);
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this period with the specified period added.
     * <p>
     * The returned period will take each unit in the provider and add the value
     * to the amount already stored in this period, returning a new one.
     * If this period does not contain an amount for the unit then the unit and
     * amount are simply returned directly in the result. The result will have
     * the union of the units in this instance and the units in the specified instance.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param periodProvider  the period to add, not null
     * @return a new period with the specified period added, never null
     * @throws ArithmeticException if the calculation overflows
     */
    public PeriodFields plus(PeriodProvider periodProvider) {
        checkNotNull(periodProvider, "PeriodProvider must not be null");
        if (this == ZERO && periodProvider instanceof PeriodFields) {
            return (PeriodFields) periodProvider;
        }
        TreeMap<PeriodUnit, PeriodField> copy = clonedMap();
        for (PeriodUnit unit : periodProvider.periodRules()) {
            long amount = periodProvider.periodAmount(unit);
            PeriodField old = copy.get(unit);
            PeriodField field = (old != null ? old.plus(amount) : PeriodField.of(amount, unit));
            copy.put(unit, field);
        }
        return create(copy);
    }

    /**
     * Returns a copy of this period with the specified period added.
     * <p>
     * The result will contain the units and amounts from this period plus the
     * specified unit and amount.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param amount  the amount to add, measured in the specified unit, may be negative
     * @param unit  the unit defining the amount, not null
     * @return a new period with the specified unit and amount added, never null
     * @throws ArithmeticException if the calculation overflows
     */
    public PeriodFields plus(long amount, PeriodUnit unit) {
        checkNotNull(unit, "PeiodRule must not be null");
        if (amount == 0) {
            return this;
        }
        TreeMap<PeriodUnit, PeriodField> copy = clonedMap();
        PeriodField old = copy.get(unit);
        PeriodField field = (old != null ? old.plus(amount) : PeriodField.of(amount, unit));
        copy.put(unit, field);
        return create(copy);
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a copy of this period with the specified period subtracted.
     * <p>
     * The returned period will take each unit in the provider and subtract the
     * value from the amount already stored in this period, returning a new one.
     * If this period does not contain an amount for the unit then the unit and
     * amount are simply returned directly in the result. The result will have
     * the union of the units in this instance and the units in the specified instance.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param period  the period to subtract, not null
     * @return a new period with the specified period subtracted, never null
     * @throws ArithmeticException if the calculation overflows
     */
    public PeriodFields minus(PeriodProvider periodProvider) {
        checkNotNull(periodProvider, "PeriodProvider must not be null");
        if (this == ZERO && periodProvider instanceof PeriodFields) {
            return (PeriodFields) periodProvider;
        }
        TreeMap<PeriodUnit, PeriodField> copy = clonedMap();
        for (PeriodUnit unit : periodProvider.periodRules()) {
            long amount = periodProvider.periodAmount(unit);
            PeriodField old = copy.get(unit);
            PeriodField field = (old != null ? old.minus(amount) : PeriodField.of(amount, unit).negated());
            copy.put(unit, field);
        }
        return create(copy);
    }

    /**
     * Returns a copy of this period with the specified period subtracted.
     * <p>
     * The result will contain the units and amounts from this period minus the
     * specified unit and amount.
     * <p>
     * This instance is immutable and unaffected by this method call.
     *
     * @param amount  the amount to subtract, measured in the specified unit, may be negative
     * @param unit  the unit defining the amount, not null
     * @return a new period with the specified amount and unit subtracted, never null
     * @throws ArithmeticException if the calculation overflows
     */
    public PeriodFields minus(long amount, PeriodUnit unit) {
        checkNotNull(unit, "PeiodRule must not be null");
        if (amount == 0) {
            return this;
        }
        TreeMap<PeriodUnit, PeriodField> copy = clonedMap();
        PeriodField old = copy.get(unit);
        copy.put(unit, old != null ? old.minus(amount) : PeriodField.of(amount, unit).negated());
        return create(copy);
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a new instance with each amount in this period multiplied
     * by the specified scalar.
     *
     * @param scalar  the scalar to multiply by, not null
     * @return a new period multiplied by the scalar, never null
     * @throws ArithmeticException if the calculation overflows
     */
    public PeriodFields multipliedBy(int scalar) {
        if (scalar == 1 || isZero()) {
            return this;
        }
        TreeMap<PeriodUnit, PeriodField> copy = createMap();
        for (PeriodField field : this) {
            copy.put(field.getUnit(), field.multipliedBy(scalar));
        }
        return create(copy);
    }

    /**
     * Returns a new instance with each amount in this period divided
     * by the specified value.
     *
     * @param divisor  the value to divide by, not null, not zero
     * @return a new period instance with the amount divided by the divisor, never null
     * @throws ArithmeticException if dividing by zero
     */
    public PeriodFields dividedBy(int divisor) {
        if (divisor == 0) {
            throw new ArithmeticException("Cannot divide by zero");
        }
        if (divisor == 1 || isZero()) {
            return this;
        }
        TreeMap<PeriodUnit, PeriodField> copy = createMap();
        for (PeriodField field : this) {
            copy.put(field.getUnit(), field.dividedBy(divisor));
        }
        return create(copy);
    }

    /**
     * Returns a new instance with each amount in this period negated.
     *
     * @return a new period with the amount negated, never null
     * @throws ArithmeticException if the calculation overflows
     */
    public PeriodFields negated() {
        return multipliedBy(-1);
    }

    //-----------------------------------------------------------------------
    /**
     * Clone the internal data storage map.
     *
     * @return the cloned map, never null
     */
    @SuppressWarnings("unchecked")
    private TreeMap<PeriodUnit, PeriodField> clonedMap() {
        return (TreeMap) unitFieldMap.clone();
    }

    //-----------------------------------------------------------------------
    /**
     * Converts this object to a map of units to amounts.
     * <p>
     * The returned map will never be null, however it may be empty.
     * It is sorted by the unit, returning the largest first.
     * It is independent of this object - changes will not be reflected back.
     *
     * @return the independent, modifiable map of periods, never null, never contains null
     */
    public SortedMap<PeriodUnit, Long> toRuleAmountMap() {
        SortedMap<PeriodUnit, Long> map = new TreeMap<PeriodUnit, Long>(Collections.reverseOrder());
        for (PeriodField field : this) {
            map.put(field.getUnit(), field.getAmount());
        }
        return map;
    }

    /**
     * Converts this period to an estimated duration.
     * <p>
     * Each {@link PeriodUnit} contains an estimated duration for that unit.
     * This method uses that estimate to calculate a total estimated duration for
     * this period.
     *
     * @return the estimated duration of this period, may be negative
     * @throws ArithmeticException if the calculation overflows
     */
    public Duration toEstimatedDuration() {
        Duration dur = Duration.ZERO;
        for (PeriodField field : this) {
            dur = dur.plus(field.toEstimatedDuration());
        }
        return dur;
    }

    //-----------------------------------------------------------------------
    /**
     * Checks if this instance equal to the object specified.
     * <p>
     * Two <code>PeriodFields</code> instances are equal if all the contained
     * <code>PeriodField</code> instances are equal.
     *
     * @param obj  the other period to compare to, null returns false
     * @return true if this instance is equal to the specified period
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof PeriodFields) {
            PeriodFields other = (PeriodFields) obj;
            return unitFieldMap.equals(other.unitFieldMap);
        }
        return false;
    }

    /**
     * Returns the hash code for this period.
     *
     * @return a suitable hash code
     */
    @Override
    public int hashCode() {
        return unitFieldMap.hashCode();
    }

    //-----------------------------------------------------------------------
    /**
     * Returns a string representation of the period, such as '[6 Days, 13 Hours]'.
     *
     * @return a descriptive representation of the period, not null
     */
    @Override
    public String toString() {
        if (unitFieldMap.size() == 0) {
            return "[]";
        }
        StringBuilder buf = new StringBuilder();
        buf.append('[');
        for (PeriodField field : this) {
            buf.append(field.toString()).append(',').append(' '); 
        }
        buf.setLength(buf.length() - 2);
        buf.append(']');
        return buf.toString();
    }

}
