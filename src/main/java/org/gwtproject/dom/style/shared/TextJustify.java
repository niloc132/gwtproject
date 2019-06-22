package org.gwtproject.dom.style.shared;

/**
 * Enum for the 'text-justify' CSS3 property.
 */
public enum TextJustify implements HasCssName {
  AUTO {
    @Override
    public String getCssName() {
      return "auto";
    }
  },
  /**
   * @deprecated Use {@link #INTER_CHARACTER} instead
   */
  DISTRIBUTE {
    @Override
    public String getCssName() {
      return "distribute";
    }
  },
  INTER_CHARACTER {
    @Override
    public String getCssName() {
      return "inter-character";
    }
  },
  INTER_CLUSTER {
    @Override
    public String getCssName() {
      return "inter-cluster";
    }
  },
  INTER_IDEOGRAPH {
    @Override
    public String getCssName() {
      return "inter-ideograph";
    }
  },
  INTER_WORD {
    @Override
    public String getCssName() {
      return "inter-word";
    }
  },
  KASHIDA {
    @Override
    public String getCssName() {
      return "kashida";
    }
  },
  NONE {
    @Override
    public String getCssName() {
      return "none";
    }
  };

  @Override
  public abstract String getCssName();
}
