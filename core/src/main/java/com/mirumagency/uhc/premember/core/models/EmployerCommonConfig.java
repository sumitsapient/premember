package com.mirumagency.uhc.premember.core.models;

import com.mirumagency.uhc.premember.core.domain.Employer;
import com.mirumagency.uhc.premember.core.models.plan.PlanWithUrls;

import java.util.List;
import java.util.Map;

public interface EmployerCommonConfig {
    List<PlanWithUrls> getMainPlans();
    String getUhcLogoPath();
    String homePagePath();
    boolean isEmployerLogoAdded();
    String getEmployerLogoPath();
    String getEmployerLogoDescription();
    boolean hideSearchForPrescription();
    Employer getEmployer();
    Map<String, String> getTokens();
}
