/*
 * Copyright 2011 Google Inc.
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

import org.gwtproject.dom.client.Element;
import org.gwtproject.dom.style.shared.Unit;
import org.gwtproject.resources.client.ClientBundle;
import org.gwtproject.resources.client.CommonResources;
import org.gwtproject.resources.client.CssResource;
import org.gwtproject.resources.client.CssResource.ImportedWithPrefix;
import org.gwtproject.uibinder.client.UiBinder;
import org.gwtproject.uibinder.client.UiField;

/** A vertical scrollbar implemented using the browsers native scrollbar. */
public class NativeVerticalScrollbar extends AbstractNativeScrollbar implements VerticalScrollbar {

  interface NativeVerticalScrollbarUiBinder extends UiBinder<Element, NativeVerticalScrollbar> {}

  /** A ClientBundle of resources used by this widget. */
  public interface Resources extends ClientBundle {

    Resources INSTANCE = new NativeVerticalScrollbar_ResourcesImpl();

    /** The styles used in this widget. */
    @Source(Style.DEFAULT_CSS)
    Style nativeVerticalScrollbarStyle();
  }

  /**
   * A variation of {@link Resources} that renders the scrollbar semi-transparent until it is
   * hovered.
   */
  public interface ResourcesTransparant extends Resources {

    ResourcesTransparant INSTANCE = new NativeVerticalScrollbar_ResourcesTransparantImpl();

    /** The styles used in this widget. */
    @Source(StyleTransparant.DEFAULT_CSS)
    Style nativeVerticalScrollbarStyle();
  }

  /** Styles used by this widget. */
  @ImportedWithPrefix("gwt-NativeVerticalScrollbar")
  public interface Style extends CssResource {
    /** The path to the default CSS styles used by this resource. */
    String DEFAULT_CSS = "org/gwtproject/user/client/ui/NativeVerticalScrollbar.gss";

    /** Applied to the scrollbar. */
    String nativeVerticalScrollbar();
  }

  /**
   * A variation of {@link Style} that renders the scrollbar semi-transparent until it is hovered.
   */
  public interface StyleTransparant extends Style {
    /** The path to the default CSS styles used by this resource. */
    String DEFAULT_CSS = "org/gwtproject/user/client/ui/NativeVerticalScrollbarTransparent.gss";
  }

  private static NativeVerticalScrollbarUiBinder uiBinder =
      new NativeVerticalScrollbar_NativeVerticalScrollbarUiBinderImpl();

  /** Client bundle used in the uibinder template. */
  interface UiBinderBundle extends ClientBundle {

    UiBinderBundle INSTANCE = new NativeVerticalScrollbar_UiBinderBundleImpl();

    UiStyle nativeVerticalScrollbarUi();
  }

  /** Style used in uibinder template. These styles aren't intended to be extended. */
  interface UiStyle extends CssResource {
    String viewport();

    String scrollable();
  }

  private static Resources DEFAULT_RESOURCES;

  /** Get the default resources for this widget. */
  private static Resources getDefaultResources() {
    if (DEFAULT_RESOURCES == null) {
      DEFAULT_RESOURCES = new NativeVerticalScrollbar_ResourcesImpl();
    }
    return DEFAULT_RESOURCES;
  }

  /** The div inside the scrollable div that forces scrollbars to appear. */
  @UiField Element contentDiv;

  /** The scrollable div used to create a scrollbar. */
  @UiField Element scrollable;

  /** Construct a new {@link NativeVerticalScrollbar}. */
  public NativeVerticalScrollbar() {
    this(getDefaultResources());
  }

  /**
   * Construct a new {@link NativeVerticalScrollbar}.
   *
   * @param resources the resources used by this widget
   */
  public NativeVerticalScrollbar(Resources resources) {
    setElement(uiBinder.createAndBindUi(this));
    getElement().addClassName(CommonResources.getInlineBlockStyle());
    setWidth(getNativeWidth() + "px");

    // Apply the styles.
    Style style = resources.nativeVerticalScrollbarStyle();
    style.ensureInjected();
    getScrollableElement().addClassName(style.nativeVerticalScrollbar());

    // inject style used in uibinder
    UiBinderBundle.INSTANCE.nativeVerticalScrollbarUi().ensureInjected();

    // Initialize the implementation.
    ScrollImpl.get().initialize(scrollable, contentDiv);
  }

  public int getMaximumVerticalScrollPosition() {
    return getScrollableElement().getScrollHeight() - getElement().getClientHeight();
  }

  public int getMinimumVerticalScrollPosition() {
    return 0;
  }

  /**
   * Get the height in pixels of the scrollable content that the scrollbar controls.
   *
   * <p>This is not the same as the maximum scroll top position. The maximum scroll position equals
   * the <code>scrollHeight- offsetHeight</code>;
   *
   * @return the scroll height
   * @see #setScrollHeight(int)
   */
  public int getScrollHeight() {
    return contentDiv.getOffsetHeight();
  }

  public int getVerticalScrollPosition() {
    return getScrollableElement().getScrollTop();
  }

  /**
   * Set the height in pixels of the scrollable content that the scrollbar controls.
   *
   * <p>This is not the same as the maximum scroll top position. The maximum scroll position equals
   * the <code>scrollHeight- offsetHeight</code>;
   *
   * @param height the size height pixels
   */
  public void setScrollHeight(int height) {
    contentDiv.getStyle().setHeight(height, Unit.PX);
  }

  public void setVerticalScrollPosition(int position) {
    getScrollableElement().setScrollTop(position);
  }

  /**
   * Get the width of the scrollbar.
   *
   * @return the width of the scrollbar in pixels
   */
  protected int getNativeWidth() {
    return getNativeScrollbarWidth();
  }

  @Override
  protected Element getScrollableElement() {
    return scrollable;
  }
}
