package com.mirumagency.uhc.premember.core.domain.employer;

import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.NiceResource;
import lombok.Getter;

@Getter
public class MemberLoginLink {
    private static final String SHOW_MEMBER_LOGIN_OPTION_PROP = "showMemberLoginOption";
    private static final String MEMBER_LOGIN_LINK_TEXT_PROP = "memberLoginLinkText";
    private static final String MEMBER_LOGIN_LINK_PROP = "memberLoginLink";

    private final Boolean showMemberLoginOption;

    private final String memberLoginLinkText;

    private final String memberLoginLink;

    public MemberLoginLink(Boolean showMemberLoginOption, String memberLoginLinkText, String memberLoginLink) {
        this.showMemberLoginOption = showMemberLoginOption;
        this.memberLoginLinkText = memberLoginLinkText;
        this.memberLoginLink = memberLoginLink;
    }

    public static MemberLoginLink of(NiceResource niceResource){
        return new MemberLoginLink(niceResource.getBoolean(SHOW_MEMBER_LOGIN_OPTION_PROP),
                niceResource.getString(MEMBER_LOGIN_LINK_TEXT_PROP),
                niceResource.getString(MEMBER_LOGIN_LINK_PROP));
    }
}
