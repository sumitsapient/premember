package com.mirumagency.uhc.premember.core.domain.provider;

import com.mirumagency.uhc.premember.core.domain.Link;
import com.mirumagency.uhc.premember.core.domain.plans.PlanCopy;
import com.mirumagency.uhc.premember.core.domain.plans.PlanOption;
import lombok.Builder;
import lombok.Getter;

import static com.mirumagency.uhc.premember.core.domain.provider.ByOptionWithStateSpecificProvider.hasStateSpecificProvider;

@Getter
@Builder
public class ByOption {
    private static final String PLAN_NAME = "planName";
    private static final String PROVIDER_SEARCH_LINK = "providerSearchLink";
    private static final String PROVIDER_SEARCH_PDF = "providerSearchPdf";
    private static final String SUMMARY_PROVIDER_SEARCH_LINK_TEXT = "summaryProviderSearchLinkText";
    private static final String PROVIDER_SEARCH_PDF_TEXT = "providerSearchPdfText";

    final String planName;
    final Link providerLink;
    final Link pdfLink;
    final boolean hasStateSpecificProvider = false;

    public ByOption(String planName, Link providerLink, Link pdfLink) {
        this.planName = planName;
        this.providerLink = providerLink;
        this.pdfLink = pdfLink;
    }

    public static ByOption of(PlanOption planOption, PlanCopy copy) {
        final ByOption byOption = ByOption.builder()
                .planName(planOption.getOrEmpty(PLAN_NAME))
                .providerLink(Link.builder()
                        .link(planOption.getOrEmpty(PROVIDER_SEARCH_LINK))
                        .text(copy.getOrEmpty(SUMMARY_PROVIDER_SEARCH_LINK_TEXT))
                        .build())
                .pdfLink(Link.builder()
                        .link(planOption.getOrEmpty(PROVIDER_SEARCH_PDF))
                        .text(planOption.getOrEmpty(PROVIDER_SEARCH_PDF_TEXT))
                        .build())
                .build();
        if (hasStateSpecificProvider(planOption)) {
            return ByOptionWithStateSpecificProvider.of(byOption, planOption);
        }
        return byOption;
    }
}
