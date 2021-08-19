package com.mirumagency.uhc.premember.core.domain.disclaimers;

public abstract class Disclaimer {

  public abstract String getText();

  public abstract Type getType();

  @Override
  public boolean equals(Object o) {
    if (o instanceof Disclaimer) {
      return getText().equals(((Disclaimer) o).getText());
    }
    return false;
  }

  @Override
  public int hashCode() {
    return getText().hashCode();
  }

  @Override
  public String toString() {
    return getText();
  }
}
