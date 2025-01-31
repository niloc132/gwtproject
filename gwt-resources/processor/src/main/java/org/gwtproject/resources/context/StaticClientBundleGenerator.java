/*
 *
 * Copyright © ${year} ${name}
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
package org.gwtproject.resources.context;

import javax.lang.model.element.TypeElement;
import org.gwtproject.resources.ext.GeneratorContext;
import org.gwtproject.resources.ext.TreeLogger;

/**
 * Copies selected files into module output with strong names and generates the ClientBundle
 * mappings.
 */
public final class StaticClientBundleGenerator extends AbstractClientBundleGenerator {

  private final ClientBundleContext clientBundleCtx = new ClientBundleContext();

  @Override
  protected AbstractResourceContext createResourceContext(
      TreeLogger logger, GeneratorContext context, TypeElement resourceBundleType, String locale) {
    return new StaticResourceContext(
        logger.branch(TreeLogger.DEBUG, "Using static resources", null),
        context,
        resourceBundleType,
        clientBundleCtx);
  }
}
