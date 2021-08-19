package com.mirumagency.uhc.premember.core.domain.federal;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Builder
public class PlanOption {
    private String id;
    private String name;
    private String description;
    private String detailsDescription;
    private String featuredText;
    private String planBenefitsSummaryPdf;
    private PlanType type;
    private Map<String, Object> data;
}