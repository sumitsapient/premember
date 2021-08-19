package com.mirumagency.uhc.premember.core.util;

import com.day.cq.dam.api.Asset;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;

import java.io.InputStream;

public class DamUtil {

    public static InputStream getFile(ResourceResolver resourceResolver, String path) {
        Resource resource = resourceResolver.getResource(path);
        if (resource != null) {
            Asset asset = resource.adaptTo(Asset.class);
            if (asset != null) {
                return asset.getOriginal().getStream();
            }
        }
        return null;
    }
}
