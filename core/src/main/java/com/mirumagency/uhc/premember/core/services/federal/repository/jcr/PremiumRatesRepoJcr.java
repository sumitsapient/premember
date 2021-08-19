package com.mirumagency.uhc.premember.core.services.federal.repository.jcr;

import com.adobe.cq.dam.cfm.ContentElement;
import com.adobe.cq.dam.cfm.ContentFragment;
import com.mirumagency.uhc.premember.core.domain.federal.PlanName;
import com.mirumagency.uhc.premember.core.domain.federal.PlanType;
import com.mirumagency.uhc.premember.core.domain.federal.PremiumRates;
import com.mirumagency.uhc.premember.core.services.federal.repository.PlanNameRepo;
import com.mirumagency.uhc.premember.core.services.migration.Constants;
import com.mirumagency.uhc.premember.core.services.federal.repository.PremiumRatesRepo;
import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.ResourceGentleman;
import com.mirumagency.uhc.premember.core.util.ContentFragmentUtil;
import org.apache.jackrabbit.core.fs.FileSystem;
import org.apache.jackrabbit.vault.util.JcrConstants;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.apache.http.util.Asserts.notNull;


@Component(service = PremiumRatesRepo.class)
public class PremiumRatesRepoJcr implements PremiumRatesRepo {

    private static final String PREMIUM_RATES_CFM_PATH = "/conf/premember/premium-rate/settings/dam/" +
            "cfm/models/premium-rate";
    @Reference
    private ResourceGentleman resourceGentleman;

    @Reference
    private PlanNameRepo planNameRepo;

    private final String TITLE_PROP = JcrConstants.JCR_CONTENT +  FileSystem.SEPARATOR + JcrConstants.JCR_TITLE;

    @Override
    public List<PremiumRates> loadAll(String regionCode) {
        List<PlanName> planNames = planNameRepo.loadPlanTypes(PlanType.HEALTH);
        return resourceGentleman.withResolver(
                resolver -> resolver
                        .getResourceOrThrow(Constants.FEDERAL_REGIONS_PATH)
                        .getChild(regionCode)
                        .map(resource -> resource.getChildren()
                                .filter(plan -> ContentFragmentUtil.isContentFragmentModel(plan,
                                        PREMIUM_RATES_CFM_PATH))
                                .filter(plan -> planNames.stream().anyMatch(p -> plan.getString(TITLE_PROP).equals(p.getId()))))
                        .map(plans -> plans.map(plan -> plan
                                .adaptTo(ContentFragment.class)))
                        .map(planContentFragment -> planContentFragment.map(this::loadPremiumRate).collect(Collectors.toList()))
                        .orElse(Collections.emptyList()));
    }

    private PremiumRates loadPremiumRate(ContentFragment planContentFragment) {
        notNull(planContentFragment, "planContentFragment");
        return PremiumRates
                .builder()
                .selfOnly(loadEnrollmentType(planContentFragment, "selfOnly"))
                .selfPlusOne(loadEnrollmentType(planContentFragment, "selfPlusOne"))
                .selfAndFamily(loadEnrollmentType(planContentFragment, "selfAndFamily"))
                .planType(planContentFragment.getName())
                .build();
    }

    private static PremiumRates.EnrollmentType loadEnrollmentType(ContentFragment planContentFragment,
                                                                  String enrollmentType) {
        ContentElement enrollmentTypeElement = planContentFragment.getElement(enrollmentType + "EnrollmentCode");
        notNull(enrollmentTypeElement, "contentElementType");

        return PremiumRates.EnrollmentType
                .builder()
                .enrollmentCode(enrollmentTypeElement.getContent())
                .nonPostalEmployee(loadNonPostalEmployee(planContentFragment, enrollmentType))
                .postalEmployee(loadPostalEmployee(planContentFragment, enrollmentType))
                .build();
    }

    private static PremiumRates.NonPostalEmployee loadNonPostalEmployee(ContentFragment planContentFragment,
                                                                        String enrollmentType) {
        String premiumBiweeklyField = enrollmentType + "NonPostalPremiumBiweekly";
        ContentElement premiumBiweeklyElement = planContentFragment.getElement(premiumBiweeklyField);
        notNull(premiumBiweeklyElement, premiumBiweeklyField);

        String premiumMonthlyField = enrollmentType + "NonPostalPremiumMonthly";
        ContentElement premiumMonthlyElement = planContentFragment.getElement(premiumMonthlyField);
        notNull(premiumMonthlyElement, premiumMonthlyField);

        return PremiumRates.NonPostalEmployee
                .builder()
                .biweekly(Double.valueOf(premiumBiweeklyElement.getContent()))
                .monthly(Double.valueOf(premiumMonthlyElement.getContent()))
                .build();
    }

    private static PremiumRates.PostalEmployee loadPostalEmployee(ContentFragment planContentFragment,
                                                                  String enrollmentType) {
        String premiumBiweeklyCategory1Field = enrollmentType + "PostalPremiumBiweeklyCategory1";
        ContentElement premiumBiweeklyCategory1Element = planContentFragment.getElement(premiumBiweeklyCategory1Field);
        notNull(premiumBiweeklyCategory1Element, premiumBiweeklyCategory1Field);

        String premiumBiweeklyCategory2Field = enrollmentType + "PostalPremiumBiweeklyCategory2";
        ContentElement premiumBiweeklyCategory2Element = planContentFragment.getElement(premiumBiweeklyCategory2Field);
        notNull(premiumBiweeklyCategory1Element, premiumBiweeklyCategory2Field);

        return PremiumRates.PostalEmployee
                .builder()
                .biweeklyCategory1(Double.valueOf(premiumBiweeklyCategory1Element.getContent()))
                .biweeklyCategory2(Double.valueOf(premiumBiweeklyCategory2Element.getContent()))
                .build();
    }

    @Override
    public Optional<PremiumRates> load(String regionCode, String healthPlanType) {
        return resourceGentleman.withResolver(
                resolver -> resolver
                        .getResourceOrThrow(Constants.FEDERAL_REGIONS_PATH)
                        .getChild(regionCode)
                        .map(resource -> resource.getChildren()
                                .filter(plan -> ContentFragmentUtil.isContentFragmentModel(plan,
                                        PREMIUM_RATES_CFM_PATH))
                                .filter(plan -> plan.getString(TITLE_PROP).equals(healthPlanType))
                                .findFirst()
                        )
                        .flatMap(plans -> plans.map(plan -> plan.adaptTo(ContentFragment.class)))
                        .map(this::loadPremiumRate)
        );
    }
}
