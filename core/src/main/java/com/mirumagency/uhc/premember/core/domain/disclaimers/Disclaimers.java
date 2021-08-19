package com.mirumagency.uhc.premember.core.domain.disclaimers;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

import com.google.common.collect.ImmutableList;
import com.mirumagency.uhc.premember.core.domain.Employer;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Disclaimers {

  private final List<EmployerDisclaimer> list;

  public static Disclaimers empty() {
    return Disclaimers.of(ImmutableList.of());
  }

  public static Disclaimers of(List<EmployerDisclaimer> list) {
    return Disclaimers.builder().list(list).build();
  }

  public EmployerDisclaimers listFor(Employer employer) {
    return EmployerDisclaimers.of(
        list.stream()
            .filter(disclaimer -> byFundingMethod(employer, disclaimer))
            .filter(disclaimer -> byState(employer, disclaimer))
            .sorted(comparing(o -> o.getType().getOrder()))
            .collect(toList())
    );
  }

  private boolean byState(Employer employer, EmployerDisclaimer disclaimer) {
    return disclaimer.getState() == State.ALL
        || disclaimer.getState() == employer.getState()
        || (disclaimer.getState() == State.ALL_EXCEPT_NY && employer.getState() != State.NY);
  }

  private boolean byFundingMethod(Employer employer, EmployerDisclaimer disclaimer) {
    return disclaimer.getFundingMethod() == FundingMethod.ALL
        || disclaimer.getFundingMethod() == employer.getFundingMethod();
  }


}
