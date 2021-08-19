package com.mirumagency.uhc.premember.core.domain.provider;

import com.mirumagency.uhc.premember.core.domain.Link;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StateSpecificProvider {
    final String description;
    final Link link;
}
