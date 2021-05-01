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
package org.gwtproject.user.client.ui;

import org.gwtproject.i18n.shared.cldr.LocaleInfo;
import org.gwtproject.resources.client.ResourcePrototype;

public class NativeHorizontalScrollbar_NativeHorizontalScrollbarUiBinderImpl_GenBundleImpl
    implements org.gwtproject.user.client.ui
        .NativeHorizontalScrollbar_NativeHorizontalScrollbarUiBinderImpl_GenBundle {
  private static NativeHorizontalScrollbar_NativeHorizontalScrollbarUiBinderImpl_GenBundleImpl
      _instance0 =
          new NativeHorizontalScrollbar_NativeHorizontalScrollbarUiBinderImpl_GenBundleImpl();

  private void styleInitializer() {
    style =
        new org.gwtproject.user.client.ui
            .NativeHorizontalScrollbar_NativeHorizontalScrollbarUiBinderImpl_GenCss_style() {
          public String getName() {
            return "style";
          }

          private boolean injected;

          public boolean ensureInjected() {
            if (!injected) {
              injected = true;
              org.gwtproject.dom.client.StyleInjector.inject(getText());
              return true;
            }
            return false;
          }

          public String getText() {
            return LocaleInfo.getCurrentLocale().isRTL()
                ? (".JGPSNOB-b-a{position:relative;overflow:hidden}.JGPSNOB-b-b{position:absolute;right:0;bottom:0;width:100%;height:100px;overflow:auto;overflow-x:scroll;overflow-y:hidden}.JGPSNOB-b-c{height:1px}")
                : (".JGPSNOB-b-a{position:relative;overflow:hidden}.JGPSNOB-b-b{position:absolute;left:0;bottom:0;width:100%;height:100px;overflow:auto;overflow-x:scroll;overflow-y:hidden}.JGPSNOB-b-c{height:1px}");
          }

          public java.lang.String viewport() {
            return "JGPSNOB-b-a";
          }

          public java.lang.String scrollable() {
            return "JGPSNOB-b-b";
          }

          public java.lang.String content() {
            return "JGPSNOB-b-c";
          }
        };
  }

  private static class styleInitializer {
    static {
      _instance0.styleInitializer();
    }

    static org.gwtproject.user.client.ui
            .NativeHorizontalScrollbar_NativeHorizontalScrollbarUiBinderImpl_GenCss_style
        get() {
      return style;
    }
  }

  public org.gwtproject.user.client.ui
          .NativeHorizontalScrollbar_NativeHorizontalScrollbarUiBinderImpl_GenCss_style
      style() {
    return styleInitializer.get();
  }

  private static java.util.HashMap<String, ResourcePrototype> resourceMap;
  private static org.gwtproject.user.client.ui
          .NativeHorizontalScrollbar_NativeHorizontalScrollbarUiBinderImpl_GenCss_style
      style;

  public ResourcePrototype[] getResources() {
    return new ResourcePrototype[] {
      style(),
    };
  }

  public ResourcePrototype getResource(String name) {
    if (resourceMap == null) {
      resourceMap = new java.util.HashMap<String, ResourcePrototype>();
      resourceMap.put("style", style());
    }
    return resourceMap.get(name);
  }
}
