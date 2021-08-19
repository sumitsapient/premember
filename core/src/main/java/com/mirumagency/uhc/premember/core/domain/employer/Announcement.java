package com.mirumagency.uhc.premember.core.domain.employer;

import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.NiceResource;
import lombok.Getter;

@Getter
public class Announcement {

    private static final String DISPLAY_TOGGLE_PROP = "showAnnouncement";

    private final boolean showAnnouncement;

    public Announcement(boolean showAnnouncement) {
        this.showAnnouncement = showAnnouncement;
    }

    public static Announcement of(NiceResource employerData) {
        return new Announcement(employerData.getBoolean(DISPLAY_TOGGLE_PROP));
    }
}
