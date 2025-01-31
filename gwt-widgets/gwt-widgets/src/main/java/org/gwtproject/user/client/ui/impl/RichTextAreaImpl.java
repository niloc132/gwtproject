/*
 * Copyright 2007 Google Inc.
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
package org.gwtproject.user.client.ui.impl;

import elemental2.dom.HTMLIFrameElement;
import jsinterop.base.Js;
import org.gwtproject.dom.client.Element;
import org.gwtproject.event.logical.shared.HasInitializeHandlers;
import org.gwtproject.event.logical.shared.InitializeEvent;
import org.gwtproject.safehtml.shared.annotations.IsSafeHtml;
import org.gwtproject.user.client.DOM;
import org.gwtproject.user.client.Event;

/**
 * Base class for RichText platform implementations. The default version simply creates a text area
 * with no rich text support.
 *
 * <p>This is not currently used by any user-agent, but will provide a &lt;textarea&gt; fallback in
 * the event a future browser fails to implement rich text editing.
 */
public class RichTextAreaImpl {

  protected Element elem;
  protected HasInitializeHandlers owner;

  public RichTextAreaImpl() {
    elem = createElement();
  }

  public Element getElement() {
    return elem;
  }

  public String getHTML() {
    return elem.getPropertyString("value");
  }

  public String getText() {
    return elem.getPropertyString("value");
  }

  public void initElement() {
    onElementInitialized();
  }

  public boolean isEnabled() {
    return !elem.getPropertyBoolean("disabled");
  }

  public void setEnabled(boolean enabled) {
    elem.setPropertyBoolean("disabled", !enabled);
  }

  public void setFocus(boolean focused) {
    HTMLIFrameElement iframe = Js.uncheckedCast(elem);
    if (focused) {
      iframe.focus();
    } else {
      iframe.blur();
    }
  }

  public void setHTML(@IsSafeHtml String html) {
    elem.setPropertyString("value", html);
  }

  public void setOwner(HasInitializeHandlers owner) {
    this.owner = owner;
  }

  public void setText(String text) {
    elem.setPropertyString("value", text);
  }

  public void uninitElement() {}

  protected Element createElement() {
    return DOM.createTextArea();
  }

  protected void hookEvents() {
    DOM.sinkEvents(
        elem,
        Event.MOUSEEVENTS | Event.KEYEVENTS | Event.ONCHANGE | Event.ONCLICK | Event.FOCUSEVENTS);
  }

  protected void onElementInitialized() {
    hookEvents();
    if (owner != null) {
      InitializeEvent.fire(owner);
    }
  }
}
