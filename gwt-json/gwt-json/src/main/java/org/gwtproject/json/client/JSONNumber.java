/*
 * Copyright © 2019 The GWT Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.gwtproject.json.client;

/** Represents a JSON number. Numbers are represented by <code>double</code>s. */
public class JSONNumber extends JSONValue {

  private double value;

  /** Creates a new JSONNumber from the double value. */
  public JSONNumber(double value) {
    this.value = value;
  }

  /** Gets the double value this JSONNumber represents. */
  public double doubleValue() {
    return value;
  }

  @Override
  public boolean equals(Object other) {
    if (!(other instanceof JSONNumber)) {
      return false;
    }
    return value == ((JSONNumber) other).value;
  }

  /**
   * Gets the double value this JSONNumber represents.
   *
   * @deprecated See {@link #doubleValue()}
   */
  @Deprecated
  public double getValue() {
    return value;
  }

  @Override
  public int hashCode() {
    // Just use the underlying double's hashCode.
    return Double.valueOf(value).hashCode();
  }

  /** Returns <code>this</code>, as this is a JSONNumber. */
  @Override
  public JSONNumber isNumber() {
    return this;
  }

  /** Returns the JSON representation of this number. */
  @Override
  public String toString() {
    return value + "";
  }

  @Override
  Object getUnwrapper() {
    return value;
  }
}
