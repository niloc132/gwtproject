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
package org.gwtproject.media.client;

import jsinterop.base.JsPropertyMap;
import org.gwtproject.dom.client.AudioElement;
import org.gwtproject.dom.client.Document;
import org.gwtproject.dom.client.PartialSupport;

/**
 * A widget representing an &lt;audio&gt; element.
 *
 * <p><span style="color:red">Experimental API: This API is still under development and is subject
 * to change. </span> This widget may not be supported on all browsers.
 */
@PartialSupport
public class Audio extends MediaBase {

  /**
   * Using a run-time check, return true if the {@link AudioElement} is supported.
   *
   * @return true if supported, false otherwise.
   */
  // TODO: probably safe to assume that everyone supports Audio
  private static boolean isSupportedRunTime(AudioElement element) {
    return ((JsPropertyMap) element).has("canPlayType");
  }

  /**
   * Return a new {@link Audio} if supported, and null otherwise.
   *
   * @return a new {@link Audio} if supported, and null otherwise
   */
  public static Audio createIfSupported() {
    AudioElement element = Document.get().createAudioElement();
    if (!isSupportedRunTime(element)) {
      return null;
    }
    return new Audio(element);
  }

  /**
   * Runtime check for whether the audio element is supported in this browser.
   *
   * @return whether the audio element is supported
   */
  public static boolean isSupported() {
    AudioElement element = Document.get().createAudioElement();
    return isSupportedRunTime(element);
  }

  /** Protected constructor. Use {@link #createIfSupported()} to create an Audio. */
  protected Audio(AudioElement element) {
    super(element);
  }

  /**
   * Returns the attached AudioElement.
   *
   * @return the AudioElement
   */
  public AudioElement getAudioElement() {
    return getMediaElement().cast();
  }
}
