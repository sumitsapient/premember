package com.mirumagency.uhc.premember.core.filters.page404;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
class RedirectResult {
  private final boolean redirect;
  private final String redirectUrl;

  static RedirectResult noRedirect(){
    return new RedirectResult(false, null);
  }

  static RedirectResult redirect(String url){
    return new RedirectResult(true, url);
  }
}
