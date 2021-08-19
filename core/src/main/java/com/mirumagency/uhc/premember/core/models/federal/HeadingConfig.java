package com.mirumagency.uhc.premember.core.models.federal;

import lombok.Getter;
import lombok.Setter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.via.ChildResource;

import javax.inject.Inject;

@Getter
@Setter
@Model(adaptables = {SlingHttpServletRequest.class, Resource.class}, adapters = HeadingConfig.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class HeadingConfig {

    private final static String CONFIG_NODE_NAME = "headingConfig";

    @Inject
    @Via(value = CONFIG_NODE_NAME, type = ChildResource.class)
    private String titleTypeSize;

    @Inject
    @Via(value = CONFIG_NODE_NAME, type = ChildResource.class)
    private String titleSizeStyle;

    @Inject
    @Via(value = CONFIG_NODE_NAME, type = ChildResource.class)
    private String subtitleTypeSize;

    @Inject
    @Via(value = CONFIG_NODE_NAME, type = ChildResource.class)
    private String subtitleSizeStyle;
}
