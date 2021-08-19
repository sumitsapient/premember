package com.mirumagency.uhc.premember.core.domain.employer;

import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.NiceResource;
import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
public class OpenEnrollment3 {

		private static final String START_DATE = "startDate3";
		private static final String END_DATE = "endDate3";
		private static final String SHOW_ENROLLMENT_DATES_FROM = "showEnrollmentDatesFrom3";
		private static final String DESCRIPTION = "enrollmentDescription3";
		private static final String OPEN_ENROLLMENT_HEADING = "openEnrollmentDateHeading3";
		private static final String DATES_RANGE_TEXT = "datesRangeText3";

		private final ZonedDateTime start;
		private final ZonedDateTime end;
		private final ZonedDateTime showFrom;
		private final String description;
		private final String openEnrollmentHeading;
		private final String datesRangeText;

		public OpenEnrollment3(ZonedDateTime start, ZonedDateTime end, ZonedDateTime showFrom, String description, String openEnrollmentHeading, String datesRangeText) {
			this.start = start;
			this.end = end;
			this.showFrom = showFrom;
			this.description = description;
			this.openEnrollmentHeading = openEnrollmentHeading;
			this.datesRangeText = datesRangeText;
		}

		public static OpenEnrollment3 of(NiceResource employerData) {
			return new OpenEnrollment3(
				employerData.getDate(START_DATE),
				employerData.getDate(END_DATE),
				employerData.getDate(SHOW_ENROLLMENT_DATES_FROM),
				employerData.getString(DESCRIPTION),
				employerData.getString(OPEN_ENROLLMENT_HEADING),
				employerData.getString(DATES_RANGE_TEXT));
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

}
