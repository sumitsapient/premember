package com.mirumagency.uhc.premember.core.models;

import com.mirumagency.uhc.premember.core.domain.Employer;
import com.mirumagency.uhc.premember.core.domain.employer.OpenEnrollment;
import com.mirumagency.uhc.premember.core.domain.employer.OpenEnrollment2;
import com.mirumagency.uhc.premember.core.domain.employer.OpenEnrollment3;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.time.ZonedDateTime;
import java.time.format.TextStyle;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Optional;

import static org.apache.commons.lang.StringUtils.isNotEmpty;

@Model(adaptables = SlingHttpServletRequest.class,
        adapters = HomepageHero.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
	public class HomepageHero extends TokensForEmployer {

    private static final String IMAGE_CHILD_RESOURCE_NAME = "image";
    private static final String TEXT_CHILD_RESOURCE_NAME = "text";
		private static final String ADDITIONAL_IMAGE_CHILD_RESOURCE_NAME = "additionalInfoImagePath";
		private static final String ADDITIONAL_TEXT_CHILD_RESOURCE_NAME = "additionalInfoDescription";

    @ScriptVariable
    private Resource resource;

    @ValueMapValue
    private String title;

    @ValueMapValue
    private String titleType;

		@ValueMapValue
		private String additionalInfoTitle;

		@ValueMapValue
		private String additionalInfoTitleType;

    @ValueMapValue
		private Boolean showButton = true;

		@ValueMapValue
		private Boolean showAdditionalInfoLinkOne = true;

		@ValueMapValue
		private Boolean showAdditionalInfoLinkTwo = true;

		@ValueMapValue
		private Boolean showAdditionalInfo = false;

    private Boolean textEmpty = true;

    private Boolean imageEmpty = true;

		private Boolean additionalTextEmpty = true;

		private Boolean additionalImageEmpty = true;

		private String additionalInfoDescription;

		private boolean hideEnrollmentDatesSection;

		private String noEnrollmentDatesText;

		private String additionalInfoLinkOne;

		private String additionalInfoLinkOneCopy;

		private String additionalInfoLinkOneNewTab;

		private String additionalInfoLinkTwo;

		private String additionalInfoLinkTwoCopy;

		private String additionalInfoLinkTwoNewTab;

    private EnrollmentDateDisplay startDate;

    private EnrollmentDateDisplay endDate;

    private long showEnrollmentDatesFrom;

    private long hideEnrollmentDatesAfter;

    private String enrollmentDescription;

    private String enrollmentHeading;

    private String datesRangeText;

    private EnrollmentDateDisplay startDate2;

    private EnrollmentDateDisplay endDate2;

    private long showEnrollmentDatesFrom2;

    private long hideEnrollmentDatesAfter2;

    private String enrollmentDescription2;

    private EnrollmentDateDisplay startDate3;

    private EnrollmentDateDisplay endDate3;

    private long showEnrollmentDatesFrom3;

    private long hideEnrollmentDatesAfter3;

    private String enrollmentDescription3;

    private String enrollmentHeading3;

    private String datesRangeText3;

    private String imagePath;

		private String additionalInfoImagePath;

    private String enrollmentGeneralDescription;

    @PostConstruct
    protected void initModel() {
        resolveTextResource();
        resolveImageResource();
				resolveAdditionalImageResource();
        resolveEnrollmentConfiguration();
        resolveEnrollment2Configuration();
        resolveEnrollment3Configuration();
        imagePath();
    }

    private void resolveEnrollmentConfiguration() {
        startDate = Optional.of(getEmployer())
                .map(Employer::getOpenEnrollment)
                .map(OpenEnrollment::getStart)
                .map(EnrollmentDateDisplay::new)
                .orElse(new EnrollmentDateDisplay());

        endDate = Optional.of(getEmployer())
                .map(Employer::getOpenEnrollment)
                .map(OpenEnrollment::getEnd)
                .map(EnrollmentDateDisplay::new)
                .orElse(new EnrollmentDateDisplay());

        showEnrollmentDatesFrom = Optional.of(getEmployer())
                .map(Employer::getOpenEnrollment)
                .map(OpenEnrollment::getShowFrom)
                // if date not provided, assume enrollment date from the start of today
                .orElse(ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS))
                .toInstant()
                .toEpochMilli();

        hideEnrollmentDatesAfter = Optional.of(getEmployer())
                .map(Employer::getOpenEnrollment)
                .map(oe -> calculateHideDate(oe.getStart(), oe.getEnd(), oe.getShowFrom()))
                .orElse(ZonedDateTime.now())
                .toInstant()
                .toEpochMilli();

        enrollmentDescription = Optional.of(getEmployer())
            .map(Employer::getOpenEnrollment)
            .map(OpenEnrollment::getDescription)
            .orElse("");

        enrollmentHeading = Optional.of(getEmployer())
            .map(Employer::getOpenEnrollment)
            .map(OpenEnrollment::getOpenEnrollmentDateHeading)
            .orElse("");

        datesRangeText = Optional.of(getEmployer())
                .map(Employer::getOpenEnrollment)
                .map(OpenEnrollment::getDatesRangeText)
                .orElse("");

        enrollmentGeneralDescription = Optional.of(getEmployer())
                .map(Employer::getOpenEnrollment)
                .map(OpenEnrollment::getEnrollmentGeneralDescription)
                .orElse("");

        additionalInfoImagePath = Optional.of(getEmployer())
                .map(Employer::getOpenEnrollment)
                .map(OpenEnrollment::getAdditionalInfoImagePath)
                .orElse("");

        additionalInfoTitle = Optional.of(getEmployer())
                .map(Employer::getOpenEnrollment)
                .map(OpenEnrollment::getAdditionalInfoTitle)
                .orElse("");

        additionalInfoDescription = Optional.of(getEmployer())
                .map(Employer::getOpenEnrollment)
                .map(OpenEnrollment::getAdditionalInfoDescription)
                .orElse("");

        noEnrollmentDatesText = Optional.of(getEmployer())
                .map(Employer::getOpenEnrollment)
                .map(OpenEnrollment::getNoEnrollmentDatesText)
                .orElse("");

        hideEnrollmentDatesSection = Optional.of(getEmployer())
                .map(Employer::getOpenEnrollment)
                .map(OpenEnrollment::getHideEnrollmentDatesSection)
                .orElse(false);
    }

    private void resolveEnrollment2Configuration() {
        startDate2 = Optional.of(getEmployer())
                .map(Employer::getOpenEnrollment2)
                .map(OpenEnrollment2::getStart)
                .map(EnrollmentDateDisplay::new)
                .orElse(new EnrollmentDateDisplay());

        endDate2 = Optional.of(getEmployer())
                .map(Employer::getOpenEnrollment2)
                .map(OpenEnrollment2::getEnd)
                .map(EnrollmentDateDisplay::new)
                .orElse(new EnrollmentDateDisplay());

        showEnrollmentDatesFrom2 = Optional.of(getEmployer())
                .map(Employer::getOpenEnrollment2)
                .map(OpenEnrollment2::getShowFrom)
                // if date not provided, assume enrollment date from the start of today
                .orElse(ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS))
                .toInstant()
                .toEpochMilli();

        hideEnrollmentDatesAfter2 = Optional.of(getEmployer())
                .map(Employer::getOpenEnrollment2)
                .map(oe -> calculateHideDate(oe.getStart(), oe.getEnd(), oe.getShowFrom()))
                .orElse(ZonedDateTime.now())
                .toInstant()
                .toEpochMilli();

        enrollmentDescription2 = Optional.of(getEmployer())
            .map(Employer::getOpenEnrollment2)
            .map(OpenEnrollment2::getDescription)
            .orElse("");
    }

    private void resolveEnrollment3Configuration() {
        startDate3 = Optional.of(getEmployer())
                .map(Employer::getOpenEnrollment3)
                .map(OpenEnrollment3::getStart)
                .map(EnrollmentDateDisplay::new)
                .orElse(new EnrollmentDateDisplay());

        endDate3 = Optional.of(getEmployer())
                .map(Employer::getOpenEnrollment3)
                .map(OpenEnrollment3::getEnd)
                .map(EnrollmentDateDisplay::new)
                .orElse(new EnrollmentDateDisplay());

        showEnrollmentDatesFrom3 = Optional.of(getEmployer())
                .map(Employer::getOpenEnrollment3)
                .map(OpenEnrollment3::getShowFrom)
                // if date not provided, assume enrollment date from the start of today
                .orElse(ZonedDateTime.now().truncatedTo(ChronoUnit.DAYS))
                .toInstant()
                .toEpochMilli();

        hideEnrollmentDatesAfter3 = Optional.of(getEmployer())
                .map(Employer::getOpenEnrollment3)
                .map(oe -> calculateHideDate(oe.getStart(), oe.getEnd(), oe.getShowFrom()))
                .orElse(ZonedDateTime.now())
                .toInstant()
                .toEpochMilli();

        enrollmentDescription3 = Optional.of(getEmployer())
            .map(Employer::getOpenEnrollment3)
            .map(OpenEnrollment3::getDescription)
            .orElse("");

        enrollmentHeading = Optional.of(getEmployer())
            .map(Employer::getOpenEnrollment3)
            .map(OpenEnrollment3::getOpenEnrollmentDateHeading)
            .orElse("");

        datesRangeText3 = Optional.of(getEmployer())
                .map(Employer::getOpenEnrollment3)
                .map(OpenEnrollment3::getDatesRangeText)
                .orElse("");
    }

    private void resolveTextResource() {
			textEmpty = !Optional.ofNullable(resource.getChild(TEXT_CHILD_RESOURCE_NAME))
				.map(Resource::getValueMap)
				.map(testProperties -> testProperties.get("text", String.class))
				.filter(StringUtils::isNotBlank)
				.isPresent();
    }

    private void resolveImageResource() {
			imageEmpty = !Optional.ofNullable(resource.getChild(IMAGE_CHILD_RESOURCE_NAME))
				.map(Resource::getValueMap)
				.map(imageProperties -> imageProperties.get("fileReference", String.class))
				.filter(StringUtils::isNotBlank)
				.isPresent();
    }

		private void resolveAdditionalImageResource() {
			additionalImageEmpty = !Optional.ofNullable(resource.getChild(ADDITIONAL_IMAGE_CHILD_RESOURCE_NAME))
				.map(Resource::getValueMap)
				.map(imageProperties -> imageProperties.get("fileReference", String.class))
				.filter(StringUtils::isNotBlank)
				.isPresent();
		}

    private void imagePath(){
        imagePath = resource.getChild(IMAGE_CHILD_RESOURCE_NAME).getValueMap().get("fileReference", String.class);
    }

    public Boolean isTextNotEmpty() {
        return !textEmpty;
    }

    public Boolean isImageNotEmpty() {
        return !imageEmpty;
    }

    public Boolean isTitleNotEmpty() {
        return isNotEmpty(title);
    }

		public Boolean isAdditionalTextNotEmpty() {
			return !additionalTextEmpty;
		}

		public Boolean isAdditionalImageNotEmpty() {
			return !additionalImageEmpty;
		}

		public Boolean isShowButton() { return showButton; }

		public Boolean isShowAdditionalInfoLinkOne() { return showAdditionalInfoLinkOne; }

		public Boolean isShowAdditionalInfoLinkTwo() { return showAdditionalInfoLinkTwo; }

		public Boolean isShowAdditionalInfo() { return showAdditionalInfo; }

		public String getTitle() {
        return tokenizeInViewMode(title);
    }

		public String getAdditionalInfoTitle() {
			return additionalInfoTitle;
		}

		public String getTitleType() {
        return titleType;
    }


    public EnrollmentDateDisplay getStartDate() {
        return startDate;
    }

    public EnrollmentDateDisplay getEndDate() {
        return endDate;
    }

    public long getShowEnrollmentDatesFrom() {
        return showEnrollmentDatesFrom;
    }

    public long hideEnrollmentDatesAfter() {
        return hideEnrollmentDatesAfter;
    }

    public String getEnrollmentDescription() {
        return enrollmentDescription;
    }

    public String getOpenEnrollmentDateHeading() {
    	return enrollmentHeading;
    }

    public String getDatesRangeText() {
    	return datesRangeText;
    }

    public EnrollmentDateDisplay getStartDate2() {
        return startDate2;
    }

    public EnrollmentDateDisplay getEndDate2() {
        return endDate2;
    }

    public long getShowEnrollmentDatesFrom2() {
        return showEnrollmentDatesFrom2;
    }

    public long hideEnrollmentDatesAfter2() {
        return hideEnrollmentDatesAfter2;
    }

    public String getEnrollmentDescription2() {
        return enrollmentDescription2;
    }

    public EnrollmentDateDisplay getStartDate3() {
        return startDate3;
    }

    public EnrollmentDateDisplay getEndDate3() {
        return endDate3;
    }

    public long getShowEnrollmentDatesFrom3() {
        return showEnrollmentDatesFrom3;
    }

    public long hideEnrollmentDatesAfter3() {
        return hideEnrollmentDatesAfter3;
    }

    public String getEnrollmentDescription3() {
        return enrollmentDescription3;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getEnrollmentGeneralDescription() {
        return enrollmentGeneralDescription;
    }

    public String getAdditionalInfoImagePath(){
        return additionalInfoImagePath;
    }

		public String getAdditionalInfoLinkOne(){
			return additionalInfoLinkOne;
		}

		public String getAdditionalInfoLinkOneCopy(){
			return additionalInfoLinkOneCopy;
		}

		public String getAdditionalInfoLinkTwo() {
			return additionalInfoLinkTwo;
		}

		public String getAdditionalInfoLinkTwoCopy() {
			return additionalInfoLinkTwoCopy;
		}

		public String getAdditionalInfoLinkOneNewTab() {
			return additionalInfoLinkOneNewTab;
		}

		public String getAdditionalInfoLinkTwoNewTab() {
			return additionalInfoLinkTwoNewTab;
		}

		public String getAdditionalInfoDescription() {
			return additionalInfoDescription;
		}

		public String getNoEnrollmentDatesText() { return noEnrollmentDatesText; }

		public boolean getHideEnrollmentDatesSection() { return hideEnrollmentDatesSection; }

    private ZonedDateTime calculateHideDate(ZonedDateTime start, ZonedDateTime end, ZonedDateTime showFrom) {
        boolean endsAtStartDate = start != null && showFrom != null && end == null;
        if (!endsAtStartDate) {
            return end;
        }
        return start;
    }

    public static class EnrollmentDateDisplay {

        private int day;
        private final String monthShort;
        private final String monthFull;

        private EnrollmentDateDisplay(ZonedDateTime zonedDateTime) {
            day = zonedDateTime.getDayOfMonth();
            monthShort = StringUtils.upperCase(
                    zonedDateTime.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
            );
            monthFull = zonedDateTime.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH);
        }

        private EnrollmentDateDisplay() {
            monthShort = StringUtils.EMPTY;
            monthFull = StringUtils.EMPTY;
        }

        public int getDay() {
            return day;
        }

        public String getMonthShort() {
            return monthShort;
        }

        public String getMonthFull() {
            return monthFull;
        }
    }
}
