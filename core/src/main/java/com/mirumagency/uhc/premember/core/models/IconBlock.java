package com.mirumagency.uhc.premember.core.models;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.util.List;

@Model(adaptables = SlingHttpServletRequest.class,
        adapters = IconBlock.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class IconBlock extends WithVisibilityOptions {

    @ValueMapValue
    private String title;

    @ValueMapValue
    private String titleTag;

    @ValueMapValue
    private String iconBlockText;

    @ValueMapValue
    private String layoutVariant;

		@ValueMapValue
		private String iconVersion;

    public String getTitle() {
        return tokenizeInViewMode(title);
    }

    public String getTitleTag() {
        return titleTag;
    }

    public String getIconBlockText() {
        return tokenizeInViewMode(iconBlockText);
    }

    public Layout getLayout() {
        return new Layout(layoutVariant);
    }

		public String getIconVersion() {
			return iconVersion;
		}

    public static class Layout {
        private static final String DEFAULT = "default";
        private static final String VERTICAL = "vertical";
        private static final String SIDE_BY_SIDE = "side-by-side";
        private static final String SIDE_BY_SIDE_WITH_TEXT_CENTERED = "side-by-side-with-text-centered";
        private static final List<String> variants = Lists.newArrayList(DEFAULT, VERTICAL, SIDE_BY_SIDE, SIDE_BY_SIDE_WITH_TEXT_CENTERED);
        private final String variant;

        private Layout(String layoutVariant) {
            if (Strings.isNullOrEmpty(layoutVariant) || !variants.contains(layoutVariant)) {
                variant = DEFAULT;
            } else {
                variant = layoutVariant;
            }
        }

        public Boolean isDefault() {
            return DEFAULT.equals(variant);
        }

        public Boolean isVertical() {
            return VERTICAL.equals(variant);
        }

        public Boolean isSideBySide() {
            return SIDE_BY_SIDE.equals(variant) || SIDE_BY_SIDE_WITH_TEXT_CENTERED.equals(variant);
        }

        public Boolean isSideBySideWithTextCentered() {
            return SIDE_BY_SIDE_WITH_TEXT_CENTERED.equals(variant);
        }
    }
}
