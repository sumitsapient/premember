package com.mirumagency.uhc.premember.core.models;

import com.mirumagency.uhc.premember.core.exceptions.RequiredResourceNotFoundException;
import com.mirumagency.uhc.premember.core.services.SvgService;
import org.apache.commons.lang.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Optional;

@Model(adaptables = SlingHttpServletRequest.class,
        adapters = Icon.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class Icon extends TokensForEmployer {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    public static final String ICON_DEFAULT = "/content/dam/premember/icons/ic_question.svg";
    @Inject
    private SvgService svgService;

    @ValueMapValue
    private String path;

    private String svgContent;

    @PostConstruct
    protected void initModel() {
        try {
            loadSvgContent();
        } catch (RequiredResourceNotFoundException e) {
            logger.warn("Invalid path to icon: {}", getPath());
        }
    }

    private void loadSvgContent() {
        svgContent = Optional.ofNullable(getPath())
                .map(svgService::getSvgContentFromAssetPath)
                .orElse(StringUtils.EMPTY);
    }

    public String getPath() {
        return tokenize(path);
    }

    public String getSvgContent() {
        return svgContent;
    }
}