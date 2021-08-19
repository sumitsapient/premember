package com.mirumagency.uhc.premember.core.models;

import com.day.cq.wcm.api.Page;
import com.mirumagency.uhc.premember.core.models.plan.PlanWithUrls;
import com.mirumagency.uhc.premember.core.services.repository.jcr.paths.PlanPage;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter
public class SitemapItem {

    private final List<SitemapItem> subItems;

    private final String pageName;

    private final String pagePath;

    private SitemapItem(String pageName, String pagePath) {
        this.pageName = pageName;
        this.pagePath = pagePath;
        this.subItems = new ArrayList<>();
    }

    private SitemapItem(Page planPage, PlanWithUrls plan) {
        this.pageName = planPage.getTitle();
        this.pagePath = planPage.getPath();
        this.subItems = new ArrayList<>();
        loadSubItems(planPage, plan);
    }

    private void loadSubItems(Page planPage, PlanWithUrls plan) {
        planPage.listChildren().forEachRemaining(subItem -> addSubItem(subItem, plan));
    }

    private void addSubItem(Page subItem, PlanWithUrls plan) {
        if (PlanPage.isPlanDetailsPage(subItem.getPath())) {
            List<SitemapItem> detailsItems = plan.getDetailsPages()
                    .stream()
                    .map(it -> new SitemapItem(it.getName(), it.getUrl()))
                    .collect(toList());
            subItems.addAll(detailsItems);
        } else {
            subItems.add(new SitemapItem(subItem.getTitle(), subItem.getPath()));
        }
    }

    private String firstOptionDetailsPageUrl(PlanWithUrls plan) {
        return plan.getSummaryPageUrl() + "/plan-details." + plan.getFirstOptionId();
    }

    static SitemapItem simple(Page planPage, PlanWithUrls plan) {
        return new SitemapItem(planPage.getTitle(), plan.getSummaryPageUrl());
    }

    static SitemapItem withSubItems(Page planPage, PlanWithUrls plan) {
        return new SitemapItem(planPage, plan);
    }

    static SitemapItem of(Page page) {
        return new SitemapItem(page.getTitle(), page.getPath());
    }
}
