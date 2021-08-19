package com.mirumagency.uhc.premember.core.models;

import com.day.cq.wcm.api.WCMMode;
import com.mirumagency.uhc.premember.core.converters.TokenizedTextConverter;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;

import java.util.Map;

import static com.mirumagency.uhc.premember.core.converters.TokenizedTextConverter.firstTokenName;
import static com.mirumagency.uhc.premember.core.converters.TokenizedTextConverter.hasTokens;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.http.util.Asserts.notNull;

@Model(adaptables = SlingHttpServletRequest.class,
        adapters = Tokens.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public abstract class Tokens extends Component {

    @Self
    private SlingHttpServletRequest request;

    protected String tokenizeInViewMode(String text) {
        if (isAuthoringMode()) {
            return text;
        }
        return tokenize(text);
    }

    protected String tokenize(String text) {
        if (isEmpty(text)) {
            return text;
        }
        return TokenizedTextConverter.convert(text, getTokens());
    }

    protected String resolveToken(String token) {
        notNull(token, "token");
        String tokenWithoutDelimiters = tokenWithoutDelimiters(token);
        return getTokens().get(tokenWithoutDelimiters);
    }

    private String tokenWithoutDelimiters(String token) {
        if (hasTokens(token)) {
            return firstTokenName(token);
        }
        return token;
    }

    protected boolean isAuthoringMode() {
        return WCMMode.fromRequest(request).equals(WCMMode.EDIT);
    }

    protected abstract Map<String, String> getTokens();
}
