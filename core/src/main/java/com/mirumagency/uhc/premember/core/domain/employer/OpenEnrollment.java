package com.mirumagency.uhc.premember.core.domain.employer;

import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.NiceResource;
import java.time.ZonedDateTime;
import lombok.Getter;

@Getter
public class OpenEnrollment {

  private static final String START_DATE = "startDate";
  private static final String END_DATE = "endDate";
  private static final String SHOW_ENROLLMENT_DATES_FROM = "showEnrollmentDatesFrom";
  private static final String DESCRIPTION = "enrollmentDescription";
  private static final String OPEN_ENROLLMENT_HEADING = "openEnrollmentDateHeading";
  private static final String DATES_RANGE_TEXT = "datesRangeText";
    private static final String ADDITIONAL_INFORMATION_IMAGE_PATH = "additionalInfoImagePath";
    private static final String ADDITIONAL_INFORMATION_TITLE = "additionalInfoTitle";
  private static final String ADDITIONAL_INFORMATION_DESCRIPTION = "additionalInfoDescription";
  private static final String GENERAL_DESCRIPTION = "enrollmentGeneralDescription";
	private static final String NO_ENROLLMENT_DATES_TEXT = "noEnrollmentDatesText";
	private static final String DISPLAY_TOGGLE_PROP = "hideEnrollmentDatesSection";

  private final ZonedDateTime start;
  private final ZonedDateTime end;
  private final ZonedDateTime showFrom;
  private final String description;
  private final String openEnrollmentHeading;
  private final String datesRangeText;
  private final String additionalInfoImagePath;
  private final String additionalInfoTitle;
  private final String additionalInfoDescription;
  private final String enrollmentGeneralDescription;
	private final String noEnrollmentDatesText;
	private final boolean hideEnrollmentDatesSection;

  public OpenEnrollment(ZonedDateTime start, ZonedDateTime end, ZonedDateTime showFrom, String description, String openEnrollmentHeading, String datesRangeText, String enrollmentGeneralDescription, String additionalInfoImagePath, String additionalInfoTitle, String additionalInfoDescription, String noEnrollmentDatesText, boolean hideEnrollmentDatesSection) {
    this.start = start;
    this.end = end;
    this.showFrom = showFrom;
    this.description = description;
    this.openEnrollmentHeading = openEnrollmentHeading;
    this.datesRangeText = datesRangeText;
    this.additionalInfoImagePath = additionalInfoImagePath;
    this.additionalInfoTitle = additionalInfoTitle;
    this.enrollmentGeneralDescription = enrollmentGeneralDescription;
    this.additionalInfoDescription = additionalInfoDescription;
    this.noEnrollmentDatesText = noEnrollmentDatesText;
		this.hideEnrollmentDatesSection = hideEnrollmentDatesSection;
  }

  public static OpenEnrollment of(NiceResource employerData) {
    return new OpenEnrollment(
        employerData.getDate(START_DATE),
        employerData.getDate(END_DATE),
        employerData.getDate(SHOW_ENROLLMENT_DATES_FROM),
        employerData.getString(DESCRIPTION),
        employerData.getString(OPEN_ENROLLMENT_HEADING),
        employerData.getString(DATES_RANGE_TEXT),
        employerData.getString(GENERAL_DESCRIPTION),
        //Additional Information Data
        employerData.getString(ADDITIONAL_INFORMATION_IMAGE_PATH),
        employerData.getString(ADDITIONAL_INFORMATION_TITLE),
        employerData.getString(ADDITIONAL_INFORMATION_DESCRIPTION),
        employerData.getString(NO_ENROLLMENT_DATES_TEXT),
        employerData.getBoolean(DISPLAY_TOGGLE_PROP));

  }

  public boolean isStartDateBeforeEndDate() {
    if (start == null || end == null) {
      return true;
    }
    return start.isBefore(end);
  }

  public boolean isShowEnrollmentBeforeOrEqualStartDate() {
    if (showFrom == null || start == null) {
      return true;
    }
    return showFrom.isBefore(start) || showFrom.isEqual(start);
  }

  public String getOpenEnrollmentDateHeading(){
    return this.openEnrollmentHeading;
  }

  public String getDatesRangeText(){
    return this.datesRangeText;
  }

	public String getNoEnrollmentDatesText() {
		return this.noEnrollmentDatesText;
	}

  public boolean getHideEnrollmentDatesSection() {
  	return this.hideEnrollmentDatesSection;
	}
}
