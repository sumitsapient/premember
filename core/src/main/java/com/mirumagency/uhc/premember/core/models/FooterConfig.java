package com.mirumagency.uhc.premember.core.models;

import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = SlingHttpServletRequest.class,
    adapters = FooterConfig.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
@Getter
public class FooterConfig extends Component {

    @ValueMapValue
    private String customerServicePhone;

    @ValueMapValue
    private String operationHours;

    @ValueMapValue
    private boolean displayMemberSupport;

    @ValueMapValue
    private String projectId;

    @ValueMapValue
    private String assetNumber;

    public String getCustomerServicePhone() {
        return StringUtils.defaultIfBlank(customerServicePhone, StringUtils.EMPTY);
    }

    public String getOperationHours() {
        return StringUtils.defaultIfBlank(operationHours, StringUtils.EMPTY);
    }
}
