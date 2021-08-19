package com.mirumagency.uhc.premember.core.domain.federal;


import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HealthBenefitsOfficer {
    private final String name;
    private final String description;
    private final String email;
    private final String phoneNumber;
    private final String photoFilePath;
}
