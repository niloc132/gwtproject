/*
 * Copyright 2008 Google Inc.
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

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.FrameElement;
import com.google.gwt.dom.client.IFrameElement;
import com.google.gwt.event.dom.client.HasLoadHandlers;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import org.gwtproject.safehtml.shared.SafeUri;
import org.gwtproject.safehtml.shared.annotations.IsTrustedResourceUri;

/**
 * A widget that wraps an IFRAME element, which can contain an arbitrary web
 * site.
 * 
 * <p>Note that if you are using {@link com.google.gwt.user.client.History}, any
 * browser history items generated by the Frame will interleave with your
 * application's history.</p>
 *
 * <h3>CSS Style Rules</h3>
 * <ul class='css'>
 * <li>.gwt-Frame { }</li>
 * </ul>
 *
 * <p>
 * <h3>Example</h3> {@example com.google.gwt.examples.FrameExample}
 * </p>
 */
public class Frame extends Widget implements HasLoadHandlers {

  static final String DEFAULT_STYLENAME = "gwt-Frame";

  /**
   * Creates a Frame widget that wraps an existing &lt;frame&gt; element.
   * 
   * This element must already be attached to the document. If the element is
   * removed from the document, you must call
   * {@link RootPanel#detachNow(Widget)}.
   * 
   * @param element the element to be wrapped
   */
  public static Frame wrap(Element element) {
    // Assert that the element is attached.
    assert Document.get().getBody().isOrHasChild(element);

    Frame frame = new Frame(element);

    // Mark it attached and remember it for cleanup.
    frame.onAttach();
    RootPanel.detachOnWindowClose(frame);

    return frame;
  }

  /**
   * Creates an empty frame.
   */
  public Frame() {
    setElement(Document.get().createIFrameElement());
    setStyleName(DEFAULT_STYLENAME);
  }

  /**
   * Creates a frame that displays the resource at the specified URL.
   *
   * @param url the URL of the resource to be displayed
   */
  public Frame(@IsTrustedResourceUri String url) {
    this();
    setUrl(url);
  }

  /**
   * This constructor may be used by subclasses to explicitly use an existing
   * element. This element must be an &lt;iframe&gt; element.
   * 
   * @param element the element to be used
   */
  protected Frame(Element element) {
    IFrameElement.as(element);
    setElement(element);
  }

  /**
   * Adds a {@link LoadEvent} load handler which will be called when the frame
   * loads.
   * 
   * @param handler the load handler
   * @return {@link HandlerRegistration} that can be used to remove this handler
   */
  public HandlerRegistration addLoadHandler(LoadHandler handler) {
    return addDomHandler(handler, LoadEvent.getType());
  }

  /**
   * Gets the URL of the frame's resource.
   * 
   * @return the frame's URL
   */
  public String getUrl() {
    return getFrameElement().getSrc();
  }

  /**
   * Sets the URL of the resource to be displayed within the frame.
   *
   * @param url the frame's new URL
   */
  public void setUrl(@IsTrustedResourceUri String url) {
    getFrameElement().setSrc(url);
  }

  /**
   * Sets the URL of the resource to be displayed within the frame.
   *
   * @param url the frame's new URL
   */
  public void setUrl(@IsTrustedResourceUri SafeUri url) {
    getFrameElement().setSrc(url);
  }

  private FrameElement getFrameElement() {
    return getElement().cast();
  }
}
