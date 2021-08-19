package com.mirumagency.uhc.premember.core.domain.employer;

import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.NiceResource;
import java.time.ZonedDateTime;
import lombok.Getter;

@Getter
public class OpenEnrollment2 {

  private static final String START_DATE = "startDate2";
  private static final String END_DATE = "endDate2";
  private static final String SHOW_ENROLLMENT_DATES_FROM = "showEnrollmentDatesFrom2";
  private static final String DESCRIPTION = "enrollmentDescription2";

  private final ZonedDateTime start;
  private final ZonedDateTime end;
  private final ZonedDateTime showFrom;
  private final String description;

  public OpenEnrollment2(ZonedDateTime start, ZonedDateTime end, ZonedDateTime showFrom, String description) {
    this.start = start;
    this.end = end;
    this.showFrom = showFrom;
    this.description = description;
  }

  public static OpenEnrollment2 of(NiceResource employerData) {
    return new OpenEnrollment2(
        employerData.getDate(START_DATE),
        employerData.getDate(END_DATE),
        employerData.getDate(SHOW_ENROLLMENT_DATES_FROM),
        employerData.getString(DESCRIPTION));
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

}
