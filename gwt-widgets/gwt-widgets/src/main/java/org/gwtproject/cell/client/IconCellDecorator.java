/*
 * Copyright 2010 Google Inc.
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
package org.gwtproject.cell.client;

import java.util.Set;
import org.gwtproject.dom.client.Element;
import org.gwtproject.dom.client.NativeEvent;
import org.gwtproject.i18n.shared.cldr.LocaleInfo;
import org.gwtproject.resources.client.ImageResource;
import org.gwtproject.safecss.shared.SafeStyles;
import org.gwtproject.safecss.shared.SafeStylesBuilder;
import org.gwtproject.safecss.shared.SafeStylesUtils;
import org.gwtproject.safehtml.client.SafeHtmlTemplates;
import org.gwtproject.safehtml.shared.SafeHtml;
import org.gwtproject.safehtml.shared.SafeHtmlBuilder;
import org.gwtproject.safehtml.shared.SafeHtmlUtils;
import org.gwtproject.user.client.ui.AbstractImagePrototype;
import org.gwtproject.user.client.ui.HasVerticalAlignment;
import org.gwtproject.user.client.ui.HasVerticalAlignment.VerticalAlignmentConstant;

/**
 * A {@link org.gwtproject.cell.client.Cell} decorator that adds an icon to another {@link
 * org.gwtproject.cell.client.Cell}.
 *
 * @param <C> the type that this Cell represents
 */
public class IconCellDecorator<C> implements org.gwtproject.cell.client.Cell<C> {

  interface Template extends SafeHtmlTemplates {

    IconCellDecorator.Template INSTANCE = new IconCellDecorator_TemplateImpl();

    SafeHtml outerDiv(SafeStyles padding, SafeHtml icon, SafeHtml cellContents);

    /** The wrapper around the image vertically aligned to the bottom. */
    SafeHtml imageWrapperBottom(SafeStyles styles, SafeHtml image);

    /** The wrapper around the image vertically aligned to the middle. */
    SafeHtml imageWrapperMiddle(SafeStyles styles, SafeHtml image);

    /** The wrapper around the image vertically aligned to the top. */
    SafeHtml imageWrapperTop(SafeStyles styles, SafeHtml image);
  }

  /** The default spacing between the icon and the text in pixels. */
  private static final int DEFAULT_SPACING = 6;

  private final org.gwtproject.cell.client.Cell<C> cell;

  private final String direction = LocaleInfo.getCurrentLocale().isRTL() ? "right" : "left";

  private final SafeHtml iconHtml;

  private final int imageWidth;

  private final SafeStyles outerDivPadding;

  private final SafeHtml placeHolderHtml;

  /**
   * Construct a new {@link IconCellDecorator}. The icon and the content will be middle aligned by
   * default.
   *
   * @param icon the icon to use
   * @param cell the cell to decorate
   */
  public IconCellDecorator(ImageResource icon, org.gwtproject.cell.client.Cell<C> cell) {
    this(icon, cell, HasVerticalAlignment.ALIGN_MIDDLE, DEFAULT_SPACING);
  }

  /**
   * Construct a new {@link IconCellDecorator}.
   *
   * @param icon the icon to use
   * @param cell the cell to decorate
   * @param valign the vertical alignment attribute of the contents
   * @param spacing the pixel space between the icon and the cell
   */
  public IconCellDecorator(
      ImageResource icon,
      org.gwtproject.cell.client.Cell<C> cell,
      VerticalAlignmentConstant valign,
      int spacing) {
    this.cell = cell;
    this.iconHtml = getImageHtml(icon, valign, false);
    this.imageWidth = icon.getWidth() + spacing;
    this.placeHolderHtml = getImageHtml(icon, valign, true);
    this.outerDivPadding =
        SafeStylesUtils.fromTrustedString("padding-" + direction + ": " + imageWidth + "px;");
  }

  public boolean dependsOnSelection() {
    return cell.dependsOnSelection();
  }

  public Set<String> getConsumedEvents() {
    return cell.getConsumedEvents();
  }

  public boolean handlesSelection() {
    return cell.handlesSelection();
  }

  public boolean isEditing(Context context, Element parent, C value) {
    return cell.isEditing(context, getCellParent(parent), value);
  }

  public void onBrowserEvent(
      Context context, Element parent, C value, NativeEvent event, ValueUpdater<C> valueUpdater) {
    cell.onBrowserEvent(context, getCellParent(parent), value, event, valueUpdater);
  }

  public void render(Context context, C value, SafeHtmlBuilder sb) {
    SafeHtmlBuilder cellBuilder = new SafeHtmlBuilder();
    cell.render(context, value, cellBuilder);
    sb.append(
        Template.INSTANCE.outerDiv(
            outerDivPadding,
            isIconUsed(value) ? getIconHtml(value) : placeHolderHtml,
            cellBuilder.toSafeHtml()));
  }

  public boolean resetFocus(Context context, Element parent, C value) {
    return cell.resetFocus(context, getCellParent(parent), value);
  }

  public void setValue(Context context, Element parent, C value) {
    cell.setValue(context, getCellParent(parent), value);
  }

  /**
   * Get the safe HTML string that represents the icon. Override this method to change the icon
   * based on the value.
   *
   * @param value the value being rendered
   * @return the HTML string that represents the icon
   */
  protected SafeHtml getIconHtml(C value) {
    return iconHtml;
  }

  /**
   * Check if the icon should be used for the value. If the icon should not be used, a placeholder
   * of the same size will be used instead. The default implementations returns true.
   *
   * @param value the value being rendered
   * @return true to use the icon, false to use a placeholder
   */
  protected boolean isIconUsed(C value) {
    return true;
  }

  /**
   * Get the HTML representation of an image. Visible for testing.
   *
   * @param res the {@link ImageResource} to render as HTML
   * @param valign the vertical alignment
   * @param isPlaceholder if true, do not include the background image
   * @return the rendered HTML
   */
  SafeHtml getImageHtml(
      ImageResource res, VerticalAlignmentConstant valign, boolean isPlaceholder) {
    // Get the HTML for the image.
    SafeHtml image;
    if (isPlaceholder) {
      image = SafeHtmlUtils.fromSafeConstant("<div></div>");
    } else {
      AbstractImagePrototype proto = AbstractImagePrototype.create(res);
      image = proto.getSafeHtml();
    }

    // Create the wrapper based on the vertical alignment.
    SafeStylesBuilder cssStyles = new SafeStylesBuilder().appendTrustedString(direction + ":0px;");
    if (HasVerticalAlignment.ALIGN_TOP == valign) {
      return Template.INSTANCE.imageWrapperTop(cssStyles.toSafeStyles(), image);
    } else if (HasVerticalAlignment.ALIGN_BOTTOM == valign) {
      return Template.INSTANCE.imageWrapperBottom(cssStyles.toSafeStyles(), image);
    } else {
      int halfHeight = (int) Math.round(res.getHeight() / 2.0);
      cssStyles.appendTrustedString("margin-top:-" + halfHeight + "px;");
      return Template.INSTANCE.imageWrapperMiddle(cssStyles.toSafeStyles(), image);
    }
  }

  /**
   * Get the parent element of the decorated cell.
   *
   * @param parent the parent of this cell
   * @return the decorated cell's parent
   */
  private Element getCellParent(Element parent) {
    return parent.getFirstChildElement().getChild(1).cast();
  }
}
