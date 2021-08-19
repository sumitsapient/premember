package com.mirumagency.uhc.premember.core.services;

import static org.apache.http.util.Asserts.notNull;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.Rendition;
import com.mirumagency.uhc.premember.core.exceptions.SvgException;
import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.NiceResource;
import com.mirumagency.uhc.premember.core.services.repository.jcr.resources.ResourceGentleman;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Utility class with static methods for handling SVG files in the DAM.
 */
@Component(service = SvgService.class)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class SvgService {

  private static final String SVG_MIME_TYPE = "image/svg+xml";

  private final Logger logger = LoggerFactory.getLogger(getClass());

  @Reference
  private ResourceGentleman resourceGentleman;

  public String getSvgContentFromAssetPath(String iconPath) {
    notNull(iconPath, "iconPath");
    return resourceGentleman.withResolver(resolver ->
        {
          Optional<String> iconContent = Optional.of(iconPath)
              .flatMap(resolver::getResource)
              .map(this::getSvgContentFromAssetResource);
          if (!iconContent.isPresent()) {
            logger.error("Icon configured in content fragment is not present in the dam: {}", iconPath);
          }
          return iconContent.orElse("");
        }
    );
  }

  private String getSvgContentFromAssetResource(NiceResource svgResource) {
    return Optional.ofNullable(svgResource)
        .map(NiceResource::getRawResource)
        .map(resource -> resource.adaptTo(Asset.class))
        .filter(this::isSvg)
        .map(this::getSvgContentFromAsset)
        .orElseThrow(
            () -> new SvgException(
                String.format("Could not retrieve SVG content from resource: %s", svgResource)));
  }

  /**
   * Determines whether an Asset is an SVG or not based on mime type
   *
   * @param asset the DAM asset
   * @return true if the asset has the SVG mime type; false otherwise
   */
  private boolean isSvg(Asset asset) {
    if (asset == null) {
      throw new SvgException("Asset cannot be null!");
    }
    return SVG_MIME_TYPE.equals(asset.getMimeType());
  }

  /**
   * Returns the contents of the SVG file inside a DAM asset as a String.
   *
   * @param asset the DAM asset
   * @return the contents of the SVG file
   */
  private String getSvgContentFromAsset(Asset asset) {
    if (asset == null) {
      throw new SvgException("Asset cannot be null!");
    }
    if (!isSvg(asset)) {
      throw new SvgException("Must be an SVG asset: " + asset.getPath());
    }
    Rendition original = asset.getOriginal();
    if (original == null) {
      throw new SvgException("SVG asset original cannot be null: " + asset.getPath());
    }
    try (InputStream stream = original.getStream()) {
      StringWriter stringWriter = new StringWriter();
      IOUtils.copy(stream, stringWriter, "UTF-8");
      return stringWriter.toString();
    } catch (IOException e) {
      throw new SvgException("Exception copying asset content to writer: " + asset.getPath(), e);
    }
  }

}
