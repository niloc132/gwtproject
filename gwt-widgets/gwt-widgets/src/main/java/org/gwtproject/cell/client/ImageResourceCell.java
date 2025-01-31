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

import org.gwtproject.resources.client.ImageResource;
import org.gwtproject.safehtml.shared.SafeHtmlBuilder;
import org.gwtproject.user.client.ui.ImageResourceRenderer;

/** An {@link AbstractCell} used to render an {@link ImageResource}. */
public class ImageResourceCell extends AbstractCell<ImageResource> {
  private static ImageResourceRenderer renderer;

  /** Construct a new ImageResourceCell. */
  public ImageResourceCell() {
    if (renderer == null) {
      renderer = new ImageResourceRenderer();
    }
  }

  @Override
  public void render(Cell.Context context, ImageResource value, SafeHtmlBuilder sb) {
    if (value != null) {
      sb.append(renderer.render(value));
    }
  }
}
