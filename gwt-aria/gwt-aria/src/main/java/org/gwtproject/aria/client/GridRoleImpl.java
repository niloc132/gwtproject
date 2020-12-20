/*
 * Copyright © 2019 The GWT Project Authors
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
package org.gwtproject.aria.client;
/////////////////////////////////////////////////////////
// This is auto-generated code.  Do not manually edit! //
/////////////////////////////////////////////////////////

import org.gwtproject.dom.client.Element;

/** Implements {@link GridRole}. */
class GridRoleImpl extends RoleImpl implements GridRole {

  GridRoleImpl(String roleName) {
    super(roleName);
  }

  @Override
  public String getAriaActivedescendantProperty(Element element) {
    return Property.ACTIVEDESCENDANT.get(element);
  }

  @Override
  public void removeAriaActivedescendantProperty(Element element) {
    Property.ACTIVEDESCENDANT.remove(element);
  }

  @Override
  public void setAriaActivedescendantProperty(Element element, Id value) {
    Property.ACTIVEDESCENDANT.set(element, value);
  }

  @Override
  public String getAriaExpandedState(Element element) {
    return State.EXPANDED.get(element);
  }

  @Override
  public void removeAriaExpandedState(Element element) {
    State.EXPANDED.remove(element);
  }

  @Override
  public void setAriaExpandedState(Element element, ExpandedValue value) {
    State.EXPANDED.set(element, value);
  }

  @Override
  public String getAriaLevelProperty(Element element) {
    return Property.LEVEL.get(element);
  }

  @Override
  public String getAriaMultiselectableProperty(Element element) {
    return Property.MULTISELECTABLE.get(element);
  }

  @Override
  public String getAriaReadonlyProperty(Element element) {
    return Property.READONLY.get(element);
  }

  @Override
  public void removeAriaLevelProperty(Element element) {
    Property.LEVEL.remove(element);
  }

  @Override
  public void removeAriaMultiselectableProperty(Element element) {
    Property.MULTISELECTABLE.remove(element);
  }

  @Override
  public void removeAriaReadonlyProperty(Element element) {
    Property.READONLY.remove(element);
  }

  @Override
  public void setAriaLevelProperty(Element element, int value) {
    Property.LEVEL.set(element, value);
  }

  @Override
  public void setAriaMultiselectableProperty(Element element, boolean value) {
    Property.MULTISELECTABLE.set(element, value);
  }

  @Override
  public void setAriaReadonlyProperty(Element element, boolean value) {
    Property.READONLY.set(element, value);
  }
}
