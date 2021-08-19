package com.mirumagency.uhc.premember.core.models;

import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.mirumagency.uhc.premember.core.util.BooleanUtil;

@Model(adaptables = SlingHttpServletRequest.class,
    adapters = Button.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Button extends WithVisibilityOptions {

  private static final String BUTTON_CLASS = "c-button";
  private static final String INVERSE_SUFFIX = "--inverse";
  private static final String NAKED_SUFFIX = "--naked";
  private static final String FULL_WIDTH = "u-w-100";
  private static final String FULL_WIDTH_MOBILE = "--full-width-mobile";

  @Inject
  private PageManager pageManager;

  @ValueMapValue
  private String text;

  @ValueMapValue
  private String href;

  @ValueMapValue
  private String type;

  @ValueMapValue
  private boolean newTab;

  @ValueMapValue
  private boolean inverse;

  @ValueMapValue
  private boolean naked;

  @ValueMapValue
  private boolean fullWidth;

  @ValueMapValue
  private boolean fullWidthMobile;

  @ValueMapValue
	private boolean setIcon;

  @ValueMapValue
  private String hiddenText;

  private String styleString;

  @ValueMapValue
  private boolean siteExitModalTrigger;

  @ValueMapValue
  private String newTabOnToken;

  @PostConstruct
  protected void initModel() {
    StringBuilder styleBuilder = new StringBuilder();
    if (StringUtils.isNotBlank(type)) {
      styleBuilder.append(type).append(" ");
    }
    if (inverse) {
      styleBuilder.append(BUTTON_CLASS).append(INVERSE_SUFFIX).append(" ");
    }
    if (naked) {
      styleBuilder.append(BUTTON_CLASS).append(NAKED_SUFFIX).append(" ");
    }
    if (fullWidth) {
      styleBuilder.append(FULL_WIDTH).append(" ");
    }
    if (fullWidthMobile) {
      styleBuilder.append(BUTTON_CLASS).append(FULL_WIDTH_MOBILE).append(" ");
    }
    styleString = styleBuilder.toString().trim();

    Page page = pageManager.getPage(href);
    if (page != null) {
      ValueMap properties = page.getProperties();
      if (properties != null) {
        String redirected = properties.get("cq:redirectTarget", "");
        boolean isInternal = false;
        if (StringUtils.isEmpty(redirected)) {
          isInternal = true;
        } else {
          if (redirected.toLowerCase().startsWith("http://") ||
              redirected.toLowerCase().startsWith("https://") ||
              redirected.toLowerCase().startsWith("//")) {
            href = redirected;
          } else {
            isInternal = true;
          }
        }
        if (isInternal) {
          href = href + ".html";
        }
      }
    }
  }

  public String getText() {
    return tokenizeInViewMode(text);
  }

  public String getHref() {
    String tokenizedHref = tokenizeInViewMode(href);
    if (tokenizedHref != null) {
      return tokenizedHref.replaceFirst("^tel:", "tel&#58;");
    }
    return null;
  }

  public String getType() {
    return type;
  }

  public boolean isNewTab() {
    return newTab || isNewTabOnToken();
  }

  public boolean isInverse() {
    return inverse;
  }

  public boolean isNaked() {
    return naked;
  }

  public boolean isFullWidth() {
    return fullWidth;
  }

  public boolean isFullWidthMobile() {
    return fullWidthMobile;
  }

  public boolean isSetIcon() { return setIcon; }

  public String getHiddenText() {
    return tokenizeInViewMode(hiddenText);
  }

  public String getStyleString() {
    return styleString;
  }

  public boolean isSiteExitModalTrigger() {
    return siteExitModalTrigger;
  }

  private boolean isNewTabOnToken() {
    return Optional.ofNullable(newTabOnToken)
        .map(this::resolveToken)
        .map(BooleanUtil::isTrue)
        .orElse(false);
  }
}
