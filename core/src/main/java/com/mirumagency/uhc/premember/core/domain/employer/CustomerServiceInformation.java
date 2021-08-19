package com.mirumagency.uhc.premember.core.domain.employer;

import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.NiceResource;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CustomerServiceInformation {

    public static final String CUSTOMER_SERVICE_PHONE_PROP = "customerServicePhone";
    public static final String OPERATION_HOURS_PROP = "operationHours";
    public static final String DISPLAY_MEMBER_SUPPORT_PROP = "displayMemberSupport";
    public static final String PROJECT_ID_PROP = "projectId";
    public static final String ASSET_NUMBER_PROP = "assetNumber";

    private String customerServicePhone;
    private String operationHours;
    private boolean displayMemberSupport;
    private String projectId;
    private String assetNumber;

    public static CustomerServiceInformation of(NiceResource footerConfig) {
        return builder()
            .customerServicePhone(footerConfig.getString(CUSTOMER_SERVICE_PHONE_PROP))
            .operationHours(footerConfig.getString(OPERATION_HOURS_PROP))
            .displayMemberSupport(footerConfig.getBoolean(DISPLAY_MEMBER_SUPPORT_PROP))
            .projectId(footerConfig.getString(PROJECT_ID_PROP))
            .assetNumber(footerConfig.getString(ASSET_NUMBER_PROP))
            .build();
    }
}
