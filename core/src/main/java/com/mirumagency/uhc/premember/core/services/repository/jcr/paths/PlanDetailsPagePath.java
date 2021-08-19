package com.mirumagency.uhc.premember.core.services.repository.jcr.paths;

import static com.mirumagency.uhc.premember.core.domain.plans.PlanType.DENTAL;
import static com.mirumagency.uhc.premember.core.domain.plans.PlanType.HEALTH;
import static com.mirumagency.uhc.premember.core.domain.plans.PlanType.VISION;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.http.util.Asserts.check;
import static org.apache.http.util.Asserts.notNull;

import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.mirumagency.uhc.premember.core.domain.plans.PlanOption;
import com.mirumagency.uhc.premember.core.domain.plans.PlanType;
import com.mirumagency.uhc.premember.core.exceptions.InvalidPlanTypeException;

public class PlanDetailsPagePath extends Path {

  public static final List<PlanType> PLANS_WITH_DETAIL_PAGE = ImmutableList
      .of(DENTAL, VISION, HEALTH);

  private static final String PLAN_DETAILS_PAGE_PATH_FORMAT = "%s/en/home/%s-plans/plan-details.%s";
  private static final String PLAN_DETAILS_PAGE_NAME_REGEXP = "/content/premember/.+/en/home/([a-z]+)-plans/plan-details(\\.(.+)_(.+))?";
  private static final String PLAN_TYPE = "Plan type";
  private static final String PLAN_OPTION = "Plan option";
  private static final String PLAN_VARIANT = "Plan vision";

  private final String path;
  private final PlanType planType;
  private final String planOptionName;
  private final String planOptionHashedVariation;

  public static boolean isPlanDetailsPagePath(String resourcePath) {
    Pattern pattern = Pattern.compile(PLAN_DETAILS_PAGE_NAME_REGEXP);
    Matcher matcher = pattern.matcher(truncHtml(resourcePath));
    return matcher.find();
  }

  public PlanDetailsPagePath(String resourcePath) {
    notNull(resourcePath, "resourcePath");
    check(isPlanDetailsPagePath(resourcePath), "Resource path must be a plan details page path!");
    this.path = truncHtml(resourcePath);
    this.planType = extractPlanType();
    this.planOptionName = extractPlanOptionName();
    this.planOptionHashedVariation = extractPlanOptionVariation();
  }

  private static String truncHtml(String path){
    int htmlStartIndex = path.indexOf(".html");
    if(htmlStartIndex > 0){
      return path.substring(0,htmlStartIndex);
    }

    return path;
  }

  private PlanType extractPlanType() {
    String planType = extractElement(1, PLAN_TYPE);
    return PlanType.from(planType).orElseThrow(() -> invalidPlanTypeException(planType, PLAN_TYPE));
  }

  private String extractPlanOptionName() {
    return extractPlanOptionElement(3, PLAN_OPTION);
  }

  private String extractPlanOptionVariation() {
    return extractPlanOptionElement(4, PLAN_VARIANT);
  }

  private String extractPlanOptionElement(int group, String elementName) {
    return Optional.ofNullable(extractElement(group, elementName))
        .orElse(EMPTY);
  }

  private String extractElement(int group, String elementName) {
    Pattern pattern = Pattern.compile(PLAN_DETAILS_PAGE_NAME_REGEXP);
    Matcher matcher = pattern.matcher(path);
    if (matcher.find()) {
      return matcher.group(group);
    }
    throw invalidPlanTypeException(path, elementName);
  }

  private InvalidPlanTypeException invalidPlanTypeException(String path, String elementName) {
    return new InvalidPlanTypeException(
        String.format("%s name from URL is invalid: %s", elementName, path));
  }

  @Override
  public String path() {
    return path;
  }

  public String planOptionId() {
    return PlanOption.id(planOptionName, planOptionHashedVariation);
  }

  public PlanType getPlanType() {
    return planType;
  }


  public String getPlanOptionName() {
    return planOptionName;
  }

  public String getPlanOptionHashedVariation() {
    return planOptionHashedVariation;
  }

  public boolean isForPlanOption(PlanOption planOption) {
    return planType == planOption.getType() && isEqualsOrEmpty(planOptionId(), planOption.getId());
  }

  private boolean isEqualsOrEmpty(String planOptionName, String name) {
    return Strings.isNullOrEmpty(planOptionName) || planOptionName.equals(name);
  }

  public static String getPlanDetailsPage(EmployerPath employerPath, PlanOption planOption) {
    notNull(employerPath, "employerPath");
    notNull(planOption, "planOption");
    return getPlanDetailPageForEmployer(employerPath.path(), planOption);
  }

  public static boolean hasPlanDetailsPage(PlanType planType) {
    return PLANS_WITH_DETAIL_PAGE.contains(planType);
  }

  private static String getPlanDetailPageForEmployer(String employerPath, PlanOption planOption) {
    return String
        .format(PLAN_DETAILS_PAGE_PATH_FORMAT, employerPath, planOption.getType().getTypeName(),
            planOption.getId());
  }
}
