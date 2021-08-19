package com.mirumagency.uhc.premember.core.filters.page404;

public interface PlansAvailability {
  default boolean available(){
    return numberPlansAvailable() >= 1;
  }
  default long numberPlansAvailable() {return 0; }
  default RedirectResult redirect() {return RedirectResult.noRedirect();}
}
