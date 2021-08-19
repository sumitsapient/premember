package com.mirumagency.uhc.premember.core.services.repository.jcr.paths;

import static java.util.regex.Pattern.compile;

import java.util.regex.Pattern;

public final class PlanPage {

  private static final Pattern COMPARISON_TABLE_REGEX = compile(
      "^/content/premember/employer.+/en/home/(\\w+)-plans/comparison-table$");
  private static final Pattern SUMMARY_REGEX = compile(
      "^/content/premember/employer.+/en/home/(\\w+)-plans$");
  private static final Pattern SEARCH_FOR_PRESCRIPTION_REGEX = compile(
      "^/content/premember/employer.+/en/home/search-for-a-prescription$");
  private static final Pattern SEARCH_FOR_PROVIDER_REGEX = compile(
          "^/content/premember/employer.+/en/home/search-for-a-provider$");
  private static final Pattern DETAILS_REGEX = compile(
      "^/content/premember/employer.+/en/home/(\\w+)-plans/plan-details$");
  private static final Pattern BENEFITS_REGEX = compile(
          "^/content/premember/employer.+/en/home/benefits$");

  public static boolean isPlanComparisonTablePage(String path) {
    return matches(path, COMPARISON_TABLE_REGEX);
  }

  public static boolean isPlanSummaryPage(String path) {
    return matches(path, SUMMARY_REGEX) || matches(path, SEARCH_FOR_PRESCRIPTION_REGEX);
  }

  public static boolean isPlanDetailsPage(String path) {
    return matches(path, DETAILS_REGEX);
  }

  public static boolean isPlanBenefitsPage(String path) {
    return matches(path, BENEFITS_REGEX);
  }

  public static boolean isSearchForPrescriptionPage(String path) {
    return matches(path, SEARCH_FOR_PRESCRIPTION_REGEX);
  }

  public static boolean isSearchForProviderPage(String path) {
    return matches(path, SEARCH_FOR_PROVIDER_REGEX);
  }

  private static boolean matches(String path, Pattern pattern) {
    return pattern.matcher(path).find();
  }
}
