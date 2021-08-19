package com.mirumagency.uhc.premember.core.domain.employer;

import static org.apache.http.util.Asserts.notNull;

import com.google.common.collect.ImmutableMap;
import com.mirumagency.uhc.premember.core.domain.Link;
import com.mirumagency.uhc.premember.core.domain.LinkTwo;
import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.NiceResource;
import java.util.Map;
import lombok.Builder;
import lombok.Builder.Default;

@Builder
public class HomePageHero {

  @Default
  private final String title = "";
  @Default
  private final String description = "";
  @Default
  private final String link = "";
  @Default
  private final String linkCopy = "";
  @Default
  private final String linkNewTab = "";
	@Default
	private final String additionalInfoTitle = "";
	@Default
	private final String additionalInfoDescription = "";

	@Default
	private final Boolean additionalInfoLinkOneOpenNewTab = false;

	@Default
	private final Boolean additionalInfoLinkTwoOpenNewTab = false;

	@Default
	private final Link linkOne = Link.empty();

	@Default
	private final LinkTwo linkTwo = LinkTwo.empty();

	private static final String ADDITIONAL_INFO_ONE_LINK_COPY = "additionalInfoLinkOneCopy";
	private static final String ADDITIONAL_INFO_ONE_LINK = "additionalInfoLinkOne";
	private static final String ADDITIONAL_INFO_ONE_LINK_NEW_TAB = "additionalInfoLinkOneNewTab";

	private static final String ADDITIONAL_INFO_TWO_LINK_COPY = "additionalInfoLinkTwoCopy";
	private static final String ADDITIONAL_INFO_TWO_LINK = "additionalInfoLinkTwo";
	private static final String ADDITIONAL_INFO_TWO_LINK_NEW_TAB = "additionalInfoLinkTwoNewTab";

	private static final String DEFAULT_OPTION_1 = "Option1";

	public static HomePageHero of(EmployerCopy copy, NiceResource employerData) {
		notNull(copy, "copy");
		notNull(employerData, "employerData");

		String optionName =
			"additionalInfo" + employerData.getStringOrElse("homePageHeroCopy", DEFAULT_OPTION_1);

		String linkOne = employerData.hasValue(ADDITIONAL_INFO_ONE_LINK)
			? employerData.getString(ADDITIONAL_INFO_ONE_LINK)
			: copy.getOrEmpty(optionName + "LinkOne");
		String linkOneText = employerData.hasValue(ADDITIONAL_INFO_ONE_LINK_COPY)
			? employerData.getString(ADDITIONAL_INFO_ONE_LINK_COPY)
			: copy.getOrEmpty(optionName + "LinkOneCopy");

		boolean oneOpenNewTab = employerData.hasValue(ADDITIONAL_INFO_ONE_LINK_NEW_TAB)
			? employerData.getBoolean(ADDITIONAL_INFO_ONE_LINK_NEW_TAB)
			: false;

			String linkTwo = employerData.hasValue(ADDITIONAL_INFO_TWO_LINK)
			? employerData.getString(ADDITIONAL_INFO_TWO_LINK)
			: copy.getOrEmpty(optionName + "LinkTwo");
		String linkTwoText = employerData.hasValue(ADDITIONAL_INFO_TWO_LINK_COPY)
			? employerData.getString(ADDITIONAL_INFO_TWO_LINK_COPY)
			: copy.getOrEmpty(optionName + "LinkTwoCopy");

		boolean twoOpenNewTab = employerData.hasValue(ADDITIONAL_INFO_TWO_LINK_NEW_TAB)
			? employerData.getBoolean(ADDITIONAL_INFO_TWO_LINK_NEW_TAB)
			: false;

    return HomePageHero.builder()
        .description(getValue(copy, employerData, "Description"))
        .link(getValue(copy, employerData, "Link"))
        .linkCopy(getValue(copy, employerData, "LinkCopy"))
        .title(employerData.getString("homePageHeroTitle"))
				.additionalInfoTitle(employerData.getString("additionalInfoTitle"))
				.additionalInfoDescription(employerData.getString("additionalInfoDescription"))
        .linkNewTab(employerData.getString("homePageHeroLinkNewTab"))
				.linkOne(Link.builder()
					.link(linkOne)
					.text(linkOneText)
					.target(oneOpenNewTab ? "_blank" : "")
					.build())
				.linkTwo(LinkTwo.builder()
					.link(linkTwo)
					.text(linkTwoText)
					.target(twoOpenNewTab ? "_blank" : "")
					.build())
        .build();
  }

	private static String getValue(EmployerCopy copy, NiceResource employerData, String field) {
		String optionName =
			"homePageHero" + employerData.getStringOrElse("homePageHeroCopy", DEFAULT_OPTION_1);
		String employerDataField = "homePageHero" + field;
		return employerData.hasValue(employerDataField)
			? employerData.getString(employerDataField)
			: copy.getOrEmpty(optionName + field);
	}

  public static HomePageHero empty() {
    return HomePageHero.builder().build();
  }

  public Map<String, String> tokens() {
			return ImmutableMap.<String, String>builder()
					.put("title", title)
					.put("description", description)
					.put("link", link)
					.put("linkCopy", linkCopy)
					.put("linkNewTab", linkNewTab)
					.put("additionalInfoTitle", additionalInfoTitle)
					.put("additionalInfoDescription", additionalInfoDescription)
					.put("additionalInfoLinkOne", linkOne.getLink())
					.put("additionalInfoLinkOneCopy", linkOne.getText())
					.put("additionalInfoLinkOneOpenNewTab", additionalInfoLinkOneOpenNewTab.toString())
					.put("additionalInfoLinkTwo", linkTwo.getLink())
					.put("additionalInfoLinkTwoCopy", linkTwo.getText())
					.put("additionalInfoLinkTwoOpenNewTab", additionalInfoLinkTwoOpenNewTab.toString())
					.build();
  }
}