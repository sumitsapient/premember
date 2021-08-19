package com.mirumagency.uhc.premember.core.models;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.mirumagency.uhc.premember.core.models.plan.PlanWithUrls;
import com.mirumagency.uhc.premember.core.services.repository.jcr.paths.PlanPage;
import lombok.Getter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Getter
@Model(
    adaptables = SlingHttpServletRequest.class,
    adapters = SitemapModel.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class SitemapModel {

  @Inject
  private PageManager pageManager;

  @Inject
  private Page currentPage;

  @Self
  private EmployerConfig employerConfig;

  private List<SitemapItem> items;

  @PostConstruct
  public void init() {
    loadSitemapItems();
    loadRegularPages();
  }

  private void loadSitemapItems() {
    items = new ArrayList<>();
    employerConfig.getMainPlans().forEach(this::loadPlanItems);
  }

  private void loadPlanItems(PlanWithUrls plan) {
    if(null != getPlanPage(plan)) {
      SitemapItem sitemapItem;
      if (plan.getOptionsCount() == 1) {
        sitemapItem = SitemapItem.simple(getPlanPage(plan), plan);
      } else {
        sitemapItem = SitemapItem.withSubItems(getPlanPage(plan), plan);
      }
      items.add(sitemapItem);
    }
  }

  private Page getPlanPage(PlanWithUrls plan) {
    return pageManager.getPage(plan.getPlanPath());
  }

  private void loadRegularPages() {
    currentPage.getParent().listChildren().forEachRemaining(page -> {
      if (isNotSitemap(page) && shouldInclude(page)) {
        items.add(SitemapItem.of(page));
      }
    });
  }

  private boolean isNotSitemap(Page page) {
    return !page.getPath().endsWith("sitemap");
  }

  private boolean shouldInclude(Page page) {
    return isBenefits(page) || isSearchForProvider(page)
            || (isSearchForPrescription(page) && canIncludeSearchForPrescription());
  }

  private boolean isBenefits(Page page) {
    return PlanPage.isPlanBenefitsPage(page.getPath());
  }

  private boolean isSearchForProvider(Page page) {
    return PlanPage.isSearchForProviderPage(page.getPath());
  }

  private boolean isSearchForPrescription(Page page) {
    return PlanPage.isSearchForPrescriptionPage(page.getPath());
  }

  private boolean canIncludeSearchForPrescription() {
    return !employerConfig.hideSearchForPrescription();
  }
}
