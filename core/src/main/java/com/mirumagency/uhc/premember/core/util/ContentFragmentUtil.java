package com.mirumagency.uhc.premember.core.util;

import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.NiceResource;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.jackrabbit.core.fs.FileSystem;
import org.apache.jackrabbit.vault.util.JcrConstants;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ContentFragmentUtil {
    public static final String CQ_MODEL = "cq:model";

    public static boolean isContentFragmentModel(NiceResource niceResource, String modelPath){
        String dataNodePath = JcrConstants.JCR_CONTENT + FileSystem.SEPARATOR + "data";
        return niceResource.getChild(dataNodePath)
                .map(dataResource -> dataResource.getString(CQ_MODEL).equals(modelPath))
                .orElse(false);
    }
}
