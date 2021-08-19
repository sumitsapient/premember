package com.mirumagency.uhc.premember.core.models;

import com.day.cq.wcm.api.Page;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.ImmutableMap;
import com.mirumagency.uhc.premember.core.domain.disclaimers.Disclaimer;
import com.mirumagency.uhc.premember.core.domain.disclaimers.FundingMethod;
import com.mirumagency.uhc.premember.core.domain.employer.CustomerServiceInformation;
import com.mirumagency.uhc.premember.core.services.EmployerService;
import com.mirumagency.uhc.premember.core.services.repository.jcr.paths.PlanPage;
import com.mirumagency.uhc.premember.core.util.ListUtil;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.apache.http.util.Asserts.notNull;

@Model(
    adaptables = SlingHttpServletRequest.class,
    adapters = Footer.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Getter
public class Footer extends TokensForEmployer {

  private static final Map<String, String> NON_DISCRIMINATION_COPY =
      ImmutableMap.of(
          "en", "Language Assistance / Non-discrimination notice",
          "es", "Asistencia de Idiomas / Aviso de no Discriminación",
          "zh", "語言協助 / 不歧視通知");

  @Self
  private EmployerConfig employerConfig;

  @SlingObject
  private Resource resource;

	@ValueMapValue
	private String footerLogo;

  @ValueMapValue
  private String contactUsTitle;

  @ValueMapValue
  private boolean contactUsTile1Override;

  @ValueMapValue
  private String contactUsTile1Title;

  @ValueMapValue
  private String contactUsTile1Description;

  @ValueMapValue
  private String contactUsTile2Title;

  @ValueMapValue
  private String contactUsTile2Description;

  @ValueMapValue
  private String contactUsTile3Title;

  private List<FooterNavigationItem> navigationItems = new ArrayList<>();

  @ValueMapValue
  private String projectId;

  @ValueMapValue
  private String assetNumber;

  @ValueMapValue
  private String copyrightInformation;

  @ValueMapValue
  private String oxfordCopyrightInformation;

  private CustomerServiceInformation customerServiceInformation;

  @Inject
  private Page currentPage;

  @Inject
  private EmployerService federalEmployerService;

  @PostConstruct
  public void init() {
    resolveNavigationItems();
    loadCustomerServiceInformation();
  }

  private void resolveNavigationItems() {
    Resource navigationItemsResource = resource.getChild("navigationItems");
    if (null != navigationItemsResource && navigationItemsResource.hasChildren()) {
      for (Resource item : navigationItemsResource.getChildren()) {
        FooterNavigationItem navItem = item.adaptTo(FooterNavigationItem.class);
        if (urlContainsHomepagePlaceholder(navItem)) {
          navItem.setUrl(substituteHomepagePlaceholder(navItem));
        }
        navigationItems.add(navItem);
      }
    }
  }

  private boolean urlContainsHomepagePlaceholder(FooterNavigationItem navItem) {
    notNull(navItem, "navItem");
    return StringUtils.contains(navItem.getUrl(), "${employerHomepage}");
  }

  private String substituteHomepagePlaceholder(FooterNavigationItem navItem) {
    String employerHomepage = employerConfig.homePagePath().replace(".html", "");
    return StrSubstitutor.replace(
        navItem.getUrl(), ImmutableMap.of("employerHomepage", employerHomepage));
  }

  private void loadCustomerServiceInformation() {
    customerServiceInformation = getEmployer().getCustomerServiceInformation();
  }

  public String getProjectId() {
    return StringUtils.defaultIfBlank(customerServiceInformation.getProjectId(), projectId);
  }

  public String getAssetNumber() {
    return StringUtils.defaultIfBlank(customerServiceInformation.getAssetNumber(), assetNumber);
  }

  public String getCustomerServicePhone() {
    return customerServiceInformation.getCustomerServicePhone();
  }

  public String getOperationHours() {
    return customerServiceInformation.getOperationHours();
  }

  public boolean shouldDisplayMemberSupport() {
    return customerServiceInformation.isDisplayMemberSupport();
  }

  public String getCopyrightInformation() {
    Calendar cal = Calendar.getInstance();
    if (employerConfig.getEmployer().getFundingMethod().equals(FundingMethod.OXFORD_ASO) ||
            employerConfig.getEmployer().getFundingMethod().equals(FundingMethod.OXFORD_FI)){
        return oxfordCopyrightInformation.replace("{dateYear}", String.valueOf(cal.get(Calendar.YEAR)));
    }
    return copyrightInformation.replace("{dateYear}", String.valueOf(cal.get(Calendar.YEAR)));
  }

  public String getEnNonDiscriminationCopy() {
    return NON_DISCRIMINATION_COPY.get("en");
  }

  public String getEsNonDiscriminationCopy() {
    return NON_DISCRIMINATION_COPY.get("es");
  }

  public String getZhNonDiscriminationCopy() {
    return NON_DISCRIMINATION_COPY.get("zh");
  }

  @SuppressWarnings("unchecked")
  public List<Disclaimer> getDisclaimers() {
    boolean isFederalSite = federalEmployerService.isFederalSite(currentPage.getPath());
    if(isFederalSite){
      return Collections.emptyList();
    }
    Builder<Disclaimer> builder = ImmutableList.builder();
    String path = resourcePath().orElse("");
    if (PlanPage.isPlanDetailsPage(path)) {
      builder.addAll(plan().getSelectedOption().getDetailsPageDisclaimers());
    }
    if (PlanPage.isPlanSummaryPage(path)) {
      builder.addAll(plan().getSummaryPageDisclaimers());
    }
    if (PlanPage.isPlanComparisonTablePage(path)) {
      builder.addAll(plan().getComparisonTablePageDisclaimers());
    }
    List<Disclaimer> legalDisclaimers = builder.build();
    List<Disclaimer> employerDisclaimers =
        getEmployer().getDisclaimers().forPlanPage(this.plan().getType());
    return ListUtil.mergeAndRemoveDuplicates(legalDisclaimers, employerDisclaimers);
  }
}
