package com.mirumagency.uhc.premember.core.domain.plans;

import lombok.Builder;
import org.apache.sling.api.resource.ValueMap;

@Builder
public class PlanPdl {

    public static final String PLAN_NAME = "planName";
    public static final String PLAN_LINK = "planLink";
    public static final String PLAN_TITLE = "planLinkTitle";
    public static final String PLAN_PDF_TITLE = "planPdfTitle";
    public static final String PLAN_PDF = "planPdf";
    public static final String PLAN_FUTURE_COVERAGE_LINK = "planFutureCoverageLink";
    public static final String PLAN_FUTURE_COVERAGE_TITLE = "planFutureCoverageTitle";

    private final String planName;
    private final String planLink;
    private final String planLinkTitle;
    private final String planPdfTitle;
    private final String planPdf;
    private final String planFutureCoverageLink;
    private final String planFutureCoverageTitle;

    public static PlanPdl of(ValueMap planData) {
       return new PlanPdl(
            planData.get(PLAN_NAME, String.class),
            planData.get(PLAN_LINK, String.class),
            planData.get(PLAN_TITLE, String.class),
            planData.get(PLAN_PDF_TITLE, String.class),
            planData.get(PLAN_PDF, String.class),
            planData.get(PLAN_FUTURE_COVERAGE_LINK, String.class),
            planData.get(PLAN_FUTURE_COVERAGE_TITLE, String.class)
        );
    }

    private static PlanPdl empty() {
        return new PlanPdl();
    }

    private PlanPdl() {
        this.planName = "";
        this.planLink = "";
        this.planLinkTitle = "";
        this.planPdfTitle = "";
        this.planPdf = "";
        this.planFutureCoverageLink = "";
        this.planFutureCoverageTitle = "";
    }

    private PlanPdl(String planName,
                    String planLink,
                    String planLinkTitle,
                    String planPdfTitle,
                    String planPdf,
                    String planFutureCoverageLink,
                    String planFutureCoverageTitle) {
        this.planName = planName;
        this.planLink = planLink;
        this.planLinkTitle = planLinkTitle;
        this.planPdfTitle = planPdfTitle;
        this.planPdf = planPdf;
        this.planFutureCoverageLink = planFutureCoverageLink;
        this.planFutureCoverageTitle = planFutureCoverageTitle;
    }

    public String getPlanName() {
        return planName;
    }

    public String getPlanLink() {
        return planLink;
    }

    public String getPlanLinkTitle() {
        return planLinkTitle;
    }

    public String getPlanPdfTitle(){return planPdfTitle;}

    public String getPlanPdf() {
        return planPdf;
    }

    public String getPlanFutureCoverageLink() {
        return planFutureCoverageLink;
    }

    public String getPlanFutureCoverageTitle() {
        return planFutureCoverageTitle;
    }
}
