package com.pryzmm.coreconfigapi.data;

import org.jetbrains.annotations.ApiStatus;

/**
 * Record of ModData
 * @param modID The mod ID
 * @param nameTranslation The translation key for the name
 * @param bannerPath Nullable; the path of the image for the banner
 * @param overrideIconPath Nullable; the path to the image to override the detected icon
 * @param backgroundColor Nullable; the background color for the UI
 * @param buttonColor Nullable; the button color for the UI
 */
@ApiStatus.Internal
public record ModData(String modID, String nameTranslation, String bannerPath, String overrideIconPath, Integer backgroundColor, Integer buttonColor) {}
