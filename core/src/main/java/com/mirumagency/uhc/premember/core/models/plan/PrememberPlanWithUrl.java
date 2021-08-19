package com.mirumagency.uhc.premember.core.models.plan;

import com.mirumagency.uhc.premember.core.domain.Employer;
import com.mirumagency.uhc.premember.core.domain.plans.Plan;
import com.mirumagency.uhc.premember.core.domain.plans.PlanOption;
import com.mirumagency.uhc.premember.core.services.repository.jcr.paths.EmployerPath;
import lombok.Builder;
import org.apache.commons.lang.StringUtils;

import java.util.List;

import static com.mirumagency.uhc.premember.core.services.repository.jcr.paths.PlanDetailsPagePath.getPlanDetailsPage;
import static com.mirumagency.uhc.premember.core.services.repository.jcr.paths.PlanDetailsPagePath.hasPlanDetailsPage;
import static com.mirumagency.uhc.premember.core.services.repository.jcr.paths.PlanPagePath.getPlanPage;
import static java.util.stream.Collectors.toList;
@Builder
public class PrememberPlanWithUrl implements PlanWithUrls {

    private static final String COPY_PLANS_NAME_HEADER = "copy.plansNameHeader";
    private static final String COPY_PLAN_ICON_CONTENT = "copy.plansIconContent";

    private final String summaryPageUrl;
    private final String name;
    private final String typeName;
    private final int optionsCount;
    private final String iconContent;
    private final PlanOption firstOption;

    private final List<PlanDetailsPage> detailsPages;

    public static PrememberPlanWithUrl of(Employer employer, Plan plan) {
        return builder()
                .name(plan.getTokens().get(COPY_PLANS_NAME_HEADER))
                .typeName(plan.getType().getTypeName())
                .optionsCount(plan.optionsCount())
                .firstOption(plan.getFirstOption())
                .iconContent(plan.getTokens().get(COPY_PLAN_ICON_CONTENT))
                .summaryPageUrl(summaryPageUrl(employer.getPath(), plan))
                .detailsPages(plan.getOptions()
                        .stream()
                        .map(option -> PrememberPlanDetailsPage.of(employer, option))
                        .collect(toList()))
                .build();
    }

    private static String summaryPageUrl(EmployerPath employerPath, Plan plan) {
        return hasOneOption(plan)
                ? getPlanDetailsPage(employerPath, plan.getFirstOption())
                : getPlanPage(employerPath, plan.getType());
    }

    public String getPlanPath() {
        return StringUtils.contains(getSummaryPageUrl(), ".")
                ? StringUtils.substringBeforeLast(getSummaryPageUrl(), "/")
                : getSummaryPageUrl();
    }

    private static boolean hasOneOption(Plan plan) {
        return plan.optionsCount() == 1 && hasPlanDetailsPage(plan.getType());
    }

    public String getFirstOptionId() {
        return firstOption.getId();
    }

    public String getName() {
        return name;
    }

    public String getTypeName() {
        return typeName;
    }

    public int getOptionsCount() {
        return optionsCount;
    }

    public String getSummaryPageUrl() {
        return summaryPageUrl;
    }

    public String getIconContent() {
        return iconContent;
    }

    public List<PlanDetailsPage> getDetailsPages() {
        return detailsPages;
    }
}
