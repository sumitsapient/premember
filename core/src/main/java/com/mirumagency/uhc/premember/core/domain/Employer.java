package com.mirumagency.uhc.premember.core.domain;

import static com.mirumagency.uhc.premember.core.util.MapUtil.mapKey;
import static com.mirumagency.uhc.premember.core.util.MapUtil.mapValue;
import static com.mirumagency.uhc.premember.core.util.MapUtil.toMap;
import static org.apache.http.util.Asserts.notNull;

import com.google.common.collect.ImmutableMap;
import com.mirumagency.uhc.premember.core.domain.disclaimers.Disclaimers;
import com.mirumagency.uhc.premember.core.domain.disclaimers.EmployerDisclaimers;
import com.mirumagency.uhc.premember.core.domain.disclaimers.FundingMethod;
import com.mirumagency.uhc.premember.core.domain.disclaimers.State;
import com.mirumagency.uhc.premember.core.domain.employer.Contact;
import com.mirumagency.uhc.premember.core.domain.employer.CustomerServiceInformation;
import com.mirumagency.uhc.premember.core.domain.employer.EmployerCopy;
import com.mirumagency.uhc.premember.core.domain.employer.Healthcare101;
import com.mirumagency.uhc.premember.core.domain.employer.HomePageHero;
import com.mirumagency.uhc.premember.core.domain.employer.MemberLoginLink;
import com.mirumagency.uhc.premember.core.domain.employer.OpenEnrollment;
import com.mirumagency.uhc.premember.core.domain.employer.OpenEnrollment2;
import com.mirumagency.uhc.premember.core.domain.employer.OpenEnrollment3;
import com.mirumagency.uhc.premember.core.domain.employer.OpenEnrollmentImage;
import com.mirumagency.uhc.premember.core.domain.employer.OptumRxCard;
import com.mirumagency.uhc.premember.core.domain.employer.HidePlanOptions;
import com.mirumagency.uhc.premember.core.domain.employer.PromotionalAreas;
import com.mirumagency.uhc.premember.core.domain.employer.*;
import com.mirumagency.uhc.premember.core.domain.plans.Plans;
import com.mirumagency.uhc.premember.core.domain.provider.CrossPlanSearch;
import com.mirumagency.uhc.premember.core.domain.provider.HomePageHeroPlanList;
import com.mirumagency.uhc.premember.core.services.repository.jcr.paths.EmployerPath;
import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.NiceResource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;

@Getter
@Builder
public class Employer {

  private static final String PLAN_OPTIONS_TITLE_PROP = "planOptionsTitle";
  private static final String SHOW_GENERIC_SITE = "showGenericSite";
  private static final String SHOW_ZIP_CODE_OPTION = "showZipCodeOption";
	private static final String SHOW_COPAYS_COST_SHARING = "showCopayAndSharing";

  public static final String OXFORD = "Oxford";

  public static final String UNITED_HEALTHCARE = "UnitedHealthCare";

  private final EmployerPath path;

  @Default
  private final EmployerName name = EmployerName.empty();

	@Default
	private final EmployerShortName shortName = EmployerShortName.empty();

  private final Logo logo;

  private final UhcLogo uhcLogo;

  private final FundingMethod fundingMethod;

  private final State state;

  private final PromotionalAreas promotionalAreas;

  @Default
  private final Healthcare101 healthcare101 = Healthcare101.empty();

  @Default
  private final HomePageHero homePageHero = HomePageHero.empty();

  private final OpenEnrollment openEnrollment;

  private final OpenEnrollment2 openEnrollment2;

  private final OpenEnrollment3 openEnrollment3;

  private final OpenEnrollmentImage openEnrollmentImage;

  private final OptumRxCard optumRxCard;

  private final HidePlanOptions hidePlanOptions;

  private final Contact contact;

  private final CustomerServiceInformation customerServiceInformation;

  private final Announcement announcement;

  private final PharmacyProviderCard pharmacyProviderCard;

  @Builder.Default
  private EmployerDisclaimers disclaimers = EmployerDisclaimers.empty();

  @Builder.Default
  private Plans plans = Plans.empty();

  @Builder.Default
  private EmployerCopy employerCopy = EmployerCopy.empty();

  @Builder.Default
  private Map<String, Object> employerData = Collections.emptyMap();

  private final String planOptionsTitle;

  private final Boolean showGenericSite;

  private final Boolean showZipCodeOption;

  private final Boolean showCopayAndSharing;

  private final MemberLoginLink memberLoginLink;

  private boolean showAnnouncement;

  public static Employer of(EmployerPath employerPath, NiceResource employerResource, NiceResource employerDataConfig,
      NiceResource footerConfig,
      EmployerCopy employerCopy,
      Map<String, Object> employerData,
      Map<String, Object> logoMetadata) {
			notNull(employerPath, "employerPath");
			notNull(employerResource, "employerResource");
			notNull(employerDataConfig, "employerDataConfig");
			notNull(footerConfig, "footerConfig");
			notNull(employerCopy, "employerCopy");
			return Employer.builder()
        .path(employerPath)
        .name(EmployerName.of(employerResource))
				.shortName(EmployerShortName.of(employerResource))
        .logo(Logo.createLogo(employerDataConfig, logoMetadata))
        .uhcLogo(UhcLogo.of(employerDataConfig))
        .fundingMethod(FundingMethod.of(employerDataConfig))
        .state(State.of(employerDataConfig))
        .openEnrollment(OpenEnrollment.of(employerDataConfig))
        .openEnrollment2(OpenEnrollment2.of(employerDataConfig))
        .openEnrollment3(OpenEnrollment3.of(employerDataConfig))
        .openEnrollmentImage(OpenEnrollmentImage.of(employerDataConfig))
        .optumRxCard(OptumRxCard.of(employerDataConfig))
        .hidePlanOptions(HidePlanOptions.of(employerDataConfig))
        .contact(Contact.of(employerDataConfig))
        .customerServiceInformation(CustomerServiceInformation.of(footerConfig))
        .promotionalAreas(PromotionalAreas.of(employerCopy, employerDataConfig))
        .healthcare101(Healthcare101.of(employerCopy, employerDataConfig))
        .employerCopy(employerCopy)
        .employerData(employerData)
        .homePageHero(HomePageHero.of(employerCopy, employerDataConfig))
        .announcement(Announcement.of(employerDataConfig))
        .pharmacyProviderCard(PharmacyProviderCard.of(employerDataConfig))
        .planOptionsTitle(employerDataConfig.getString(PLAN_OPTIONS_TITLE_PROP))
        .showGenericSite(employerDataConfig.getBoolean(SHOW_GENERIC_SITE))
        .showZipCodeOption(employerDataConfig.getBoolean(SHOW_ZIP_CODE_OPTION))
				.memberLoginLink(MemberLoginLink.of(employerDataConfig))
				.showCopayAndSharing(employerDataConfig.getBoolean((SHOW_COPAYS_COST_SHARING)))
        .build();
  }

  public CrossPlanSearch getCrossPlanSearch() {
    return CrossPlanSearch.of(this);
  }

  public Employer withPlans(Plans plans) {
    this.plans = plans;
    return this;
  }

  public Employer withDisclaimers(Disclaimers disclaimers) {
    this.disclaimers = disclaimers.listFor(this);
    return this;
  }

  public Map<String, String> getTokens(boolean includePlanTokens) {
    Map<String, String> tokens = new HashMap<>();
    if(includePlanTokens){
      tokens.putAll(plans.getTokens());
      tokens.putAll(tokensWithPrefix("crossPlanProviderSearch", getCrossPlanSearch().getTokens()));
    }
    tokens.putAll(tokensWithPrefix("employerCopy", employerCopy.tokens()));
    tokens.putAll(tokensWithPrefix("homePageHero", homepageHeroTokens()));
    tokens.putAll(tokensWithPrefix("employerData", employerDataTokens()));
    return ImmutableMap.<String, String>builder()
        .putAll(tokens)
        .build();
  }

  private Map<String, String> homepageHeroTokens() {
    return ImmutableMap.<String, String>builder()
				.putAll(homePageHero.tokens())
				.putAll(getHomePageHeroPlanList().getTokens())
				.build();
  }

  private HomePageHeroPlanList getHomePageHeroPlanList() {
    return HomePageHeroPlanList.of(this);
  }

  private Map<String, String> employerDataTokens() {
		return ImmutableMap.<String, String>builder()
				.putAll(toTokens(name.getTokens()))
				.putAll(toTokens(shortName.getTokens()))
				.putAll(toTokens(this.employerData))
				.putAll(healthcare101.getTokens())
				.build();
  }

  private Map<String, String> tokensWithPrefix(String prefix, Map<String, String> tokens) {
    return tokens.entrySet().stream()
        .map(e -> mapKey(e, key -> String.format("%s.%s", prefix, key)))
        .collect(toMap());
  }

  private Map<String, String> toTokens(Map<String, Object> data) {
    return data.entrySet().stream()
        .map(e -> mapValue(e, Object::toString))
        .collect(toMap());
  }

  public String getUHCorOxford(String value){
    String company = UNITED_HEALTHCARE;
    if (this.getFundingMethod().equals(FundingMethod.OXFORD_FI) ||
            this.getFundingMethod().equals(FundingMethod.OXFORD_ASO)) {
      company = OXFORD;
    }
    String uhcOrOxfordString = value.replace("{company}", company);
    return uhcOrOxfordString;
  }
}
