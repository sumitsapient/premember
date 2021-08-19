package com.mirumagency.uhc.premember.core.models;

import com.mirumagency.uhc.premember.core.domain.employer.ContactPerson;
import java.time.format.DateTimeFormatter;

import com.mirumagency.uhc.premember.core.domain.employer.MemberLoginLink;
import com.mirumagency.uhc.premember.core.domain.employer.OpenEnrollment;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = SlingHttpServletRequest.class,
    adapters = EmployerData.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class EmployerData extends TokensForEmployer {

  private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

  public String getStartDate() {
    return DATE_FORMATTER.format(getEmployer().getOpenEnrollment().getStart());
  }

  public String getEndDate() {
    return DATE_FORMATTER.format(getEmployer().getOpenEnrollment().getEnd());
  }

  public String getShowEnrollmentDateFrom() {
    return DATE_FORMATTER.format(getEmployer().getOpenEnrollment().getShowFrom());
  }

  public String getOpenEnrollmentDateHeading(){
  	return getEmployer().getOpenEnrollment().getOpenEnrollmentDateHeading();
  }

	public String getNoEnrollmentDatesText(){
  	return getEmployer().getOpenEnrollment().getNoEnrollmentDatesText();
  }

	public boolean getHideEnrollmentDatesSection() {
  	return getEmployer().getOpenEnrollment().getHideEnrollmentDatesSection();
  }

  public String getDatesRangeText(){
  	return getEmployer().getOpenEnrollment().getDatesRangeText();
  }

  public String getDescription() {
    return getEmployer().getOpenEnrollment().getDescription();
  }

  public String getStartDate2() {
    return DATE_FORMATTER.format(getEmployer().getOpenEnrollment2().getStart());
  }

  public String getEndDate2() {
    return DATE_FORMATTER.format(getEmployer().getOpenEnrollment2().getEnd());
  }

  public String getShowEnrollmentDateFrom2() {
    return DATE_FORMATTER.format(getEmployer().getOpenEnrollment2().getShowFrom());
  }

  public String getDescription2() {
    return getEmployer().getOpenEnrollment2().getDescription();
  }

  public String getStartDate3() {
    return DATE_FORMATTER.format(getEmployer().getOpenEnrollment3().getStart());
  }

  public String getEndDate3() {
    return DATE_FORMATTER.format(getEmployer().getOpenEnrollment3().getEnd());
  }

  public String getShowEnrollmentDateFrom3() {
    return DATE_FORMATTER.format(getEmployer().getOpenEnrollment3().getShowFrom());
  }

  public String getOpenEnrollmentDateHeading3() {
    return  getEmployer().getOpenEnrollment3().getOpenEnrollmentDateHeading();
  }

  public String getDatesRangeText3(){
    return getEmployer().getOpenEnrollment3().getDatesRangeText();
  }

  public String getDescription3() {
    return getEmployer().getOpenEnrollment3().getDescription();
  }

  public boolean isStartDateNotBlank() {
    return getEmployer().getOpenEnrollment().getStart() != null;
  }

  public boolean isEndDateNotBlank() {
    return getEmployer().getOpenEnrollment().getEnd() != null;
  }

  public boolean isShowEnrollmentFromDateNotBlank() {
    return getEmployer().getOpenEnrollment().getShowFrom() != null;
  }

  public boolean isStartDate2NotBlank() {
    return getEmployer().getOpenEnrollment2().getStart() != null;
  }

  public boolean isEndDate2NotBlank() {
    return getEmployer().getOpenEnrollment2().getEnd() != null;
  }

  public boolean isShowEnrollmentFromDate2NotBlank() {
    return getEmployer().getOpenEnrollment2().getShowFrom() != null;
  }

  public boolean isStartDate3NotBlank() {
    return getEmployer().getOpenEnrollment3().getStart() != null;
  }

  public boolean isEndDate3NotBlank() {
    return getEmployer().getOpenEnrollment3().getEnd() != null;
  }

  public boolean isShowEnrollmentFromDate3NotBlank() {
    return getEmployer().getOpenEnrollment3().getShowFrom() != null;
  }

  public String getEmployerLogoPath() {
    return getEmployer().getLogo().getPath();
  }

  public String getUhcLogoPath() {
    return getEmployer().getUhcLogo().getPath();
  }

  public String getOpenEnrollmentImagePath() {
    return getEmployer().getOpenEnrollmentImage().getPath();
  }

  public boolean isStartDateBeforeEndDate() {
    return getEmployer().getOpenEnrollment().isStartDateBeforeEndDate();
  }

  public boolean isShowEnrollmentBeforeOrEqualStartDate() {
    return getEmployer().getOpenEnrollment().isShowEnrollmentBeforeOrEqualStartDate();
  }

  public boolean isStartDate2BeforeEndDate2() {
    return getEmployer().getOpenEnrollment2().isStartDateBeforeEndDate();
  }

  public boolean isShowEnrollment2BeforeOrEqualStartDate2() {
    return getEmployer().getOpenEnrollment2().isShowEnrollmentBeforeOrEqualStartDate();
  }

  public boolean isStartDate3BeforeEndDate3() {
    return getEmployer().getOpenEnrollment3().isStartDateBeforeEndDate();
  }

  public boolean isShowEnrollment3BeforeOrEqualStartDate3() {
    return getEmployer().getOpenEnrollment3().isShowEnrollmentBeforeOrEqualStartDate();
  }

  public String getOpeningDays() {
    return getEmployer().getContact().getOpeningDays();
  }

  public String getOpeningHours() {
    return getEmployer().getContact().getOpeningHours();
  }

  public String getPhone() {
    return getEmployer().getContact().getPhoneNumber().getNumber();
  }

  public ContactPerson getPerson1() {
    return getEmployer().getContact().getFirstContactPerson();
  }

  public ContactPerson getPerson2() {
    return getEmployer().getContact().getSecondContactPerson();
  }

  public String getCardTitle() {
    return getEmployer().getContact().getCardTitle();
  }

  public String getAdditionalDetails() {
    return getEmployer().getContact().getAdditionalDetails();
  }

  public String getPlanOptionsTitle(){
      return getEmployer().getPlanOptionsTitle();
  }

  public String getShowGenericSite(){
    return getEmployer().getShowGenericSite().toString();
  }

  public boolean getShowZipCodeOption(){
      return getEmployer().getShowZipCodeOption();
  }

  public boolean getShowCopayAndSharing(){
      return getEmployer().getShowCopayAndSharing();
  }

  public MemberLoginLink getMemberLoginLink(){
      return getEmployer().getMemberLoginLink();
  }

  public String getOpenEnrollmentGeneralDescription() {
      return getEmployer().getOpenEnrollment().getEnrollmentGeneralDescription();
  }

  public String getOpenAdditionalInfoImagePath() {
    return getEmployer().getOpenEnrollment().getAdditionalInfoImagePath();
  }
}
