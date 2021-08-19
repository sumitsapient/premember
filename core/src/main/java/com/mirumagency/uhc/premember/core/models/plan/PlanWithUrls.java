package com.mirumagency.uhc.premember.core.models.plan;
import java.util.List;

public interface PlanWithUrls{
    String getPlanPath();
    String getSummaryPageUrl();
    String getName();
    String getTypeName();
    int getOptionsCount();
    String getIconContent();
    String getFirstOptionId();
    List<PlanDetailsPage> getDetailsPages();
}
