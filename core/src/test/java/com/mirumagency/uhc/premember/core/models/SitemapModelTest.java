package com.mirumagency.uhc.premember.core.models;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.google.common.collect.ImmutableList;
import com.mirumagency.uhc.premember.core.models.plan.PlanWithUrls;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SitemapModelTest {

  private static final String NOT_IMPORTANT = "";

  @Mock
  private EmployerConfig employerConfig;

  @Mock
  private PageManager pageManger;

  @Mock
  private Page currentPage;

  @Mock
  private Page page;

  @Mock
  private PlanWithUrls planWithUrls;

  @Mock
  private Page childPage;

  @Mock
  private Page searchForPrescription;

  @Mock
  private Page searchForProvider;

  @Mock
  private Page benefits;

  @InjectMocks
  private SitemapModel model;

  @BeforeEach
  public void setup() {
    // given
    List<PlanWithUrls> mainPlans = ImmutableList.of(planWithUrls);

    // when
    when(currentPage.getParent()).thenReturn(page);
    when(page.listChildren()).thenReturn(ImmutableList.of(childPage).iterator());
    when(childPage.getPath()).thenReturn(NOT_IMPORTANT);
    when(employerConfig.getMainPlans()).thenReturn(mainPlans);
    when(pageManger.getPage(any())).thenReturn(page);
  }

  @Test
  public void shouldNotCreateSubitems_whenOnlyOnePlanOptionAvailable() {
    // when
    when(planWithUrls.getOptionsCount()).thenReturn(1);
    model.init();
    List<SitemapItem> items = model.getItems();

    // then
    assertEquals(1, items.size());
    assertEquals(0, items.get(0).getSubItems().size());
  }

  @Test
  public void shouldIncludeSearchForPrescriptionSearchForProviderAndBenefitsPage() {
    // when
    when(planWithUrls.getOptionsCount()).thenReturn(1);
    when(searchForPrescription.getPath()).thenReturn("/content/premember/employers/demo/en/home/search-for-a-prescription");
    when(searchForProvider.getPath()).thenReturn("/content/premember/employers/demo/en/home/search-for-a-provider");
    when(benefits.getPath()).thenReturn("/content/premember/employer/demo/en/home/benefits");
    when(page.listChildren()).thenReturn(ImmutableList.of(
            childPage,
            searchForPrescription,
            searchForProvider,
            benefits).iterator());
    model.init();
    List<SitemapItem> items = model.getItems();

    // then
    assertEquals(4, items.size());
    assertEquals(0, items.get(0).getSubItems().size());
  }

  @Test
  public void shouldSkipSearchForPrescription_whenDisabledInConfig() {
    // when
    when(employerConfig.hideSearchForPrescription()).thenReturn(true);
    when(planWithUrls.getOptionsCount()).thenReturn(1);
    when(searchForPrescription.getPath()).thenReturn("/content/premember/employers/demo/en/home/search-for-a-prescription");
    when(searchForProvider.getPath()).thenReturn("/content/premember/employers/demo/en/home/search-for-a-provider");
    when(benefits.getPath()).thenReturn("/content/premember/employer/demo/en/home/benefits");
    when(page.listChildren()).thenReturn(ImmutableList.of(
            childPage,
            searchForPrescription,
            searchForProvider,
            benefits).iterator());
    model.init();
    List<SitemapItem> items = model.getItems();

    // then
    assertEquals(3, items.size());
    assertEquals(0, items.get(0).getSubItems().size());
  }

  @Test
  public void skipSitemap_whenOnlyOnePlanOptionAvailable() {
    // when
    when(planWithUrls.getOptionsCount()).thenReturn(1);
    when(childPage.getPath()).thenReturn("sitemap");
    model.init();
    List<SitemapItem> items = model.getItems();

    // then
    assertEquals(1, items.size());
    assertEquals(0, items.get(0).getSubItems().size());
  }

  @Test
  public void shouldCreateSubitems_whenMorePlanOptionsAvailable() {
    // when
    when(planWithUrls.getOptionsCount()).thenReturn(2);
    model.init();
    List<SitemapItem> items = model.getItems();

    // then
    assertEquals(items.size(), 1);
    assertEquals(items.get(0).getSubItems().size(), 1);
  }
}
