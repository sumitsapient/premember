package com.mirumagency.uhc.premember.core.domain.provider;

import com.mirumagency.uhc.premember.core.domain.Link;
import com.mirumagency.uhc.premember.core.domain.plans.PlanOption;
import lombok.Getter;

import static java.lang.Boolean.TRUE;

@Getter
public class ByOptionWithStateSpecificProvider extends ByOption {
    private static final String STATE_SPECIFIC_PROVIDER_DESCRIPTION = "stateSpecificProviderDescription";
    private static final String STATE_SPECIFIC_PROVIDER_LINK = "stateSpecificProviderLink";
    private static final String STATE_SPECIFIC_PROVIDER_LINK_TEXT = "stateSpecificProviderLinkText";
    private static final String HAS_STATE_SPECIFIC_PROVIDER = "hasStateSpecificProvider";

    final boolean hasStateSpecificProvider = true;
    final StateSpecificProvider stateSpecificProvider;

    public ByOptionWithStateSpecificProvider(String planName, Link providerLink, Link pdfLink, StateSpecificProvider stateSpecificProvider) {
        super(planName, providerLink, pdfLink);
        this.stateSpecificProvider = stateSpecificProvider;
    }

    public static ByOption of(ByOption byOption, PlanOption planOption) {
        final StateSpecificProvider stateSpecificProvider = StateSpecificProvider.builder()
                .description(planOption.getOrEmpty(STATE_SPECIFIC_PROVIDER_DESCRIPTION))
                .link(Link.builder()
                        .link(planOption.getOrEmpty(STATE_SPECIFIC_PROVIDER_LINK))
                        .text(planOption.getOrEmpty(STATE_SPECIFIC_PROVIDER_LINK_TEXT))
                        .build())
                .build();
        return new ByOptionWithStateSpecificProvider(byOption.planName, byOption.providerLink, byOption.pdfLink, stateSpecificProvider);
    }

    public static boolean hasStateSpecificProvider(PlanOption planOption) {
        return TRUE.equals(planOption.getDetails().get(HAS_STATE_SPECIFIC_PROVIDER));
    }
}
