package org.gwtproject.validation.rebind.beaninfo;

import java.lang.annotation.Annotation;

import javax.validation.ConstraintValidatorContext;

/**
 * Defines the logic to validate a given constraint A
 * for a given object type T.
 * Implementations must comply to the following restriction:
 * <ul>
 * <li>T must resolve to a non parameterized type</li>
 * <li>or generic parameters of T must be unbounded
 * wildcard types</li>
 * </ul>
 *
 * @author Emmanuel Bernard
 * @author Hardy Ferentschik
 */
public interface ConstraintValidator<A extends Annotation, T> {
    /**
     * Initialize the validator in preparation for isValid calls.
     * The constraint annotation for a given constraint declaration
     * is passed.
     * <p/>
     * This method is guaranteed to be called before any use of this instance for
     * validation.
     *
     * @param constraintAnnotation annotation instance for a given constraint declaration
     */
    void initialize(A constraintAnnotation);

    /**
     * Implement the validation logic.
     * The state of <code>value</code> must not be altered.
     *
     * This method can be accessed concurrently, thread-safety must be ensured
     * by the implementation.
     *
     * @param value object to validate
     * @param context context in which the constraint is evaluated
     *
     * @return false if <code>value</code> does not pass the constraint
     */
    boolean isValid(T value, ConstraintValidatorContext context);
}