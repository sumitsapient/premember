package com.mirumagency.uhc.premember.core.domain.employer;

import com.mirumagency.uhc.premember.core.domain.Copy;
import java.util.Collections;
import java.util.Map;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
public class EmployerCopy extends Copy {

  public EmployerCopy(Map<String, Object> emptyMap) {
    super(emptyMap);
  }

  public static EmployerCopy empty() {
    return new EmployerCopy(Collections.emptyMap());
  }

  public static EmployerCopy of(Map<String, Object> data) {
    return new EmployerCopy(data);
  }
}