package org.gwtproject.user.client.ui;

import org.gwtproject.resources.client.ResourcePrototype;

public class CustomScrollPanel_ResourcesImpl implements CustomScrollPanel.Resources {
  private static CustomScrollPanel_ResourcesImpl _instance0 = new CustomScrollPanel_ResourcesImpl();
  private void customScrollPanelStyleInitializer() {
    customScrollPanelStyle = new CustomScrollPanel.Style() {
      public String getName() {
        return "customScrollPanelStyle";
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
        return (".MU1SOAB-j-b{background:#efefef}");
      }
      public String customScrollPanel() {
        return "MU1SOAB-j-a";
      }
      public String customScrollPanelCorner() {
        return "MU1SOAB-j-b";
      }
    }
    ;
  }
  private static class customScrollPanelStyleInitializer {
    static {
      _instance0.customScrollPanelStyleInitializer();
    }
    static CustomScrollPanel.Style get() {
      return customScrollPanelStyle;
    }
  }
  public CustomScrollPanel.Style customScrollPanelStyle() {
    return customScrollPanelStyleInitializer.get();
  }
  private static java.util.HashMap<String, ResourcePrototype> resourceMap;
  private static CustomScrollPanel.Style customScrollPanelStyle;
  
  public ResourcePrototype[] getResources() {
    return new ResourcePrototype[] {
      customScrollPanelStyle(), 
    };
  }
  public ResourcePrototype getResource(String name) {
      if (resourceMap == null) {
        resourceMap = new java.util.HashMap<String, ResourcePrototype>();
        resourceMap.put("customScrollPanelStyle", customScrollPanelStyle());
      }
      return resourceMap.get(name);
  }
}
