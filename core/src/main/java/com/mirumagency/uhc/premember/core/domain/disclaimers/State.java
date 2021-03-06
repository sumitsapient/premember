package com.mirumagency.uhc.premember.core.domain.disclaimers;

import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.NiceResource;
import java.util.Arrays;
import lombok.Getter;

@Getter
public enum State {
  NOT_SELECTED("Not selected", "NOT_SELECTED"),
  ALL("All states", "ALL"),
  ALL_EXCEPT_NY("All except NY", "ALL_EXCEPT_NY"),
  AL("Alabama", "AL"),
  AK("Alaska", "AK"),
  AZ("Arizona", "AZ"),
  AR("Arkansas", "AR"),
  CA("California", "CA"),
  CO("Colorado", "CO"),
  CT("Connecticut", "CT"),
  DE("Delaware", "DE"),
  FL("Florida", "FL"),
  GA("Georgia", "GA"),
  HI("Hawaii", "HI"),
  ID("Idaho", "ID"),
  IL("Illinois", "IL"),
  IN("Indiana", "IN"),
  IA("Iowa", "IA"),
  KS("Kansas", "KS"),
  KY("Kentucky", "KY"),
  LA("Louisiana", "LA"),
  ME("Maine", "ME"),
  MD("Maryland", "MD"),
  MA("Massachusetts", "MA"),
  MI("Michigan", "MI"),
  MN("Minnesota", "MN"),
  MS("Mississippi", "MS"),
  MO("Missouri", "MO"),
  MT("Montana", "MT"),
  NE("Nebraska", "NE"),
  NV("Nevada", "NV"),
  NH("New Hampshire", "NH"),
  NJ("New Jersey", "NJ"),
  NM("New Mexico", "NM"),
  NY("New York", "NY"),
  NC("North Carolina", "NC"),
  ND("North Dakota", "ND"),
  OH("Ohio", "OH"),
  OK("Oklahoma", "OK"),
  OR("Oregon", "OR"),
  PA("Pennsylvania", "PA"),
  RI("Rhode Island", "RI"),
  SC("South Carolina", "SC"),
  SD("South Dakota", "SD"),
  TN("Tennessee", "TN"),
  TX("Texas", "TX"),
  UT("Utah", "UT"),
  VT("Vermont", "VT"),
  VA("Virginia", "VA"),
  WA("Washington", "WA"),
  WV("West Virginia", "WV"),
  WI("Wisconsin", "WI"),
  WY("Wyoming", "WY");

  private final String name;
  private final String code;

  State(String name, String code) {
    this.name = name;
    this.code = code;
  }

  public static State of(NiceResource niceResource) {
    final String stringValue = niceResource.getString("state");
    return Arrays.stream(State.values())
        .filter(it -> stringValue.equals(it.code)).findFirst()
        .orElse(NOT_SELECTED);
  }
}
