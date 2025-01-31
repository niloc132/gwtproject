/*
 * Copyright 2006 Google Inc.
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
package org.gwtproject.user.client.ui;

/**
 * A widget that implements this interface has a caption which can be set and retrieved using these
 * methods.
 */
public interface HasCaption {

  /**
   * Gets this widget's caption.
   *
   * @return the caption.
   */
  String getCaption();

  /**
   * Sets this widget's caption.
   *
   * @param caption the new caption.
   */
  void setCaption(String caption);
}
