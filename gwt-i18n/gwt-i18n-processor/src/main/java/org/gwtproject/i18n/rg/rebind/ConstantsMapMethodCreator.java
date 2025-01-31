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
package org.gwtproject.i18n.rg.rebind;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import javax.lang.model.element.ExecutableElement;
import org.gwtproject.i18n.ext.TreeLogger;
import org.gwtproject.i18n.shared.GwtLocale;

/** Creator for methods of the form Map getX() . */
class ConstantsMapMethodCreator extends AbstractLocalizableMethodCreator {

  static final String GENERIC_STRING_MAP_TYPE = "java.util.Map<java.lang.String, java.lang.String>";

  /**
   * Constructor for localizable returnType method creator.
   *
   * @param classCreator
   */
  public ConstantsMapMethodCreator(AbstractGeneratorClassCreator classCreator) {
    super(classCreator);
  }

  /**
   * Generates Map of key/value pairs for a list of keys.
   *
   * @param logger TreeLogger instance for logging
   * @param method method body to create
   * @param mapName name to create map from
   * @param resourceList AbstractResource for key lookup
   * @param locale locale to use for localized string lookup
   */
  @Override
  public void createMethodFor(
      TreeLogger logger,
      ExecutableElement method,
      String mapName,
      AbstractResource.ResourceList resourceList,
      GwtLocale locale) {
    String methodName = method.getSimpleName().toString();
    if (method.getParameters().size() > 0) {
      error(
          logger,
          methodName
              + " cannot have parameters; extend Messages instead if you need to create "
              + "parameterized messages");
    }
    // make sure cache exists
    enableCache();
    // check cache for array
    println(
        GENERIC_STRING_MAP_TYPE
            + " args = ("
            + GENERIC_STRING_MAP_TYPE
            + ") cache.get("
            + wrap(methodName)
            + ");");
    // if not found create Map
    println("if (args == null) {");
    indent();
    // Use a LinkedHashMap to preserve declaration order.
    println("args = new java.util.LinkedHashMap<String, String>();");

    String keyString;
    try {
      keyString = resourceList.getRequiredString(mapName);
    } catch (AbstractResource.MissingResourceException e) {
      e.setDuring("getting key list");
      throw e;
    }

    String[] keys = ConstantsStringArrayMethodCreator.split(keyString);
    AbstractResource.ResourceList resources = getResources();
    // Use a LinkedHashMap to preserve declaration order (but remove dups).
    Map<String, String> map = new LinkedHashMap<String, String>();
    for (String key : keys) {
      if (key.length() == 0) {
        continue;
      }

      try {
        // check for "map[key]=value" first
        String value = resources.getStringExt(mapName, key);
        if (value == null) {
          // for backwards compatibility, check for "key=value", which must be
          // present if the form above isn't present.
          value = resources.getRequiredString(key);
        }
        map.put(key, value);
      } catch (AbstractResource.MissingResourceException e) {
        e.setDuring("implementing map");
        throw e;
      }
    }

    for (Entry<String, String> entry : map.entrySet()) {
      println("args.put(" + wrap(entry.getKey()) + ", " + wrap(entry.getValue()) + ");");
    }
    println("args = java.util.Collections.unmodifiableMap(args);");
    println("cache.put(" + wrap(methodName) + ", args);");
    outdent();
    println("}");
    println("return args;");
  }
}
