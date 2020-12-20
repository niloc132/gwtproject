/*
 * Copyright 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package org.gwtproject.typedarrays.client;

import jsinterop.annotations.JsOverlay;
import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsProperty;
import jsinterop.annotations.JsType;
import org.gwtproject.typedarrays.shared.ArrayBuffer;
import org.gwtproject.typedarrays.shared.Int32Array;

/** JS native implementation of {@link Int32Array}. */
@JsType(isNative = true, name = "Int32Array", namespace = JsPackage.GLOBAL)
public final class Int32ArrayNative extends ArrayBufferViewNative implements Int32Array {

  /**
   * @param buffer a buffer to that this array should use for storage
   * @return a {@link Int32Array} instance
   */
  @JsOverlay
  public static Int32ArrayNative create(ArrayBuffer buffer) {
    return new Int32ArrayNative(buffer);
  }

  /** @param buffer a buffer to that this array should use for storage */
  public Int32ArrayNative(ArrayBuffer buffer) {}

  /**
   * @param buffer a buffer to that this array should use for storage
   * @param byteOffset the offset in bytes to the first item to reference in the buffer
   * @return a {@link Int32Array} instance
   */
  @JsOverlay
  public static Int32ArrayNative create(ArrayBuffer buffer, int byteOffset) {
    return new Int32ArrayNative(buffer, byteOffset);
  }

  /**
   * @param buffer a buffer to that this array should use for storage
   * @param byteOffset the offset in bytes to the first item to reference in the buffer
   */
  public Int32ArrayNative(ArrayBuffer buffer, int byteOffset) {}

  /**
   * @param buffer a buffer to that this array should use for storage
   * @param byteOffset the offset in bytes to the first item to reference in the buffer
   * @param length the number of elements in the array to reference
   * @return a {@link Int32Array} instance
   */
  @JsOverlay
  public static Int32ArrayNative create(ArrayBuffer buffer, int byteOffset, int length) {
    return new Int32ArrayNative(buffer, byteOffset, length);
  }

  /**
   * @param buffer a buffer to that this array should use for storage
   * @param byteOffset the offset in bytes to the first item to reference in the buffer
   * @param length the number of elements in the array to reference
   */
  public Int32ArrayNative(ArrayBuffer buffer, int byteOffset, int length) {}

  /**
   * @param array an array of initial values
   * @return a {@link Int32Array} instance
   */
  @JsOverlay
  public static Int32ArrayNative create(int[] array) {
    return new Int32ArrayNative(array);
  }

  /** @param array an array of initial values */
  public Int32ArrayNative(int[] array) {}

  /**
   * @param length the number of items that the created array should contain
   * @return a {@link Int32Array} instance
   */
  @JsOverlay
  public static Int32ArrayNative create(int length) {
    return new Int32ArrayNative(length);
  }

  /** @param length the number of items that the created array should contain */
  public Int32ArrayNative(int length) {}

  //  @Override
  //  @JsOverlay
  //  public int get(int index) {
  //    return Js.<JsArrayLike<Double>>uncheckedCast(this).getAt(index).intValue();
  //  }

  @Override
  @JsProperty(name = "length")
  public native int length();

  //  @Override
  //  @JsOverlay
  //  public void set(int index, int value) {
  //    Js.<JsArrayLike<Double>>uncheckedCast(this).setAt(index, (double) value);
  //  }

  @Override
  public native void set(int[] array);

  @Override
  public native void set(int[] array, int offset);

  @Override
  public native void set(Int32Array array);

  @Override
  public native void set(Int32Array array, int offset);

  @Override
  public native Int32Array subarray(int begin);

  @Override
  public native Int32Array subarray(int begin, int end);
}
