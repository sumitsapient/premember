package com.mirumagency.uhc.premember.core.models.federal;

import com.mirumagency.uhc.premember.core.domain.Employer;
import com.mirumagency.uhc.premember.core.domain.federal.Plan;
import com.mirumagency.uhc.premember.core.domain.federal.PlanOption;
import com.mirumagency.uhc.premember.core.models.plan.PlanDetailsPage;
import com.mirumagency.uhc.premember.core.models.plan.PlanWithUrls;
import lombok.Builder;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;


import static com.mirumagency.uhc.premember.core.services.federal.repository.jcr.paths.PlanPath.getPlanPage;
import static java.util.stream.Collectors.*;

@Builder
@Getter
public class FederalPlanWithUrls implements PlanWithUrls {
    private final String summaryPageUrl;

    private final String name;

    private final String typeName;

    private final int optionsCount;

    private final String iconContent;

    private final String firstOptionId;

    private final List<PlanDetailsPage> detailsPages;

    public static FederalPlanWithUrls of(Employer employer, Plan plan){
        PlanOption firstOption = plan.getFirstOption().orElse(null);
        return builder()
                .name(plan.getPlanHeaderName())
                .typeName(plan.getType().getTypeName())
                .optionsCount(plan.optionsCount())
                .firstOptionId(firstOption == null ? StringUtils.EMPTY : firstOption.getId())
                .iconContent(plan.getIconLink())
                .summaryPageUrl(summaryPageUrl(plan))
                .detailsPages(plan.getOptions()
                        .stream()
                        .map(option ->FederalPlanDetailsPage.of(employer, option))
                        .collect(toList()))
                .build();
    }

    private static String summaryPageUrl(Plan plan) {
        return getPlanPage(plan.getType());
    }

    @Override
    public String getPlanPath(){
        return StringUtils.contains(getSummaryPageUrl(), ".")
                ? StringUtils.substringBeforeLast(getSummaryPageUrl(), "/")
                : getSummaryPageUrl();
    }

}
