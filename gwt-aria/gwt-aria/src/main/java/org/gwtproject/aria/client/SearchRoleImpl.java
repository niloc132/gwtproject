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

/** Implements {@link SearchRole}. */
class SearchRoleImpl extends RoleImpl implements SearchRole {

  SearchRoleImpl(String roleName) {
    super(roleName);
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
}
