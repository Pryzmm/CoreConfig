package com.pryzmm.coreconfigapi.registrar;

import com.pryzmm.coreconfigapi.data.ModHolder;
import org.jetbrains.annotations.Nullable;

/**
 * Allows registration for mods to attach on CoreConfig with optional values
 */
public interface ConfigRegistrar {

    /**
     * Registers a mod to CoreConfig in its most basic form.
     * @param modID The mod ID
     * @param nameTranslation The translation for the name in the config
     */
    static void register(String modID, String nameTranslation) {
        register(modID, nameTranslation, null, null, null, null);
    }

    /**
     * Registers a mod to CoreConfig with a banner.
     * @param modID The mod ID
     * @param nameTranslation The translation for the name in the config
     * @param bannerPath The path to an image for a banner to be displayed on the mod
     */
    static void register(String modID, String nameTranslation, @Nullable String bannerPath) {
        register(modID, nameTranslation, bannerPath, null, null, null);
    }

    /**
     * Registers a mod to CoreConfig with a banner, and overrides the detected icon.
     * @param modID The mod ID
     * @param nameTranslation The translation for the name in the config
     * @param bannerPath The path to an image for a banner to be displayed on the mod in the CoreConfig UI
     * @param overrideIconPath The path to an image for the detected icon to be overridden with in the CoreConfig UI
     */
    static void register(String modID, String nameTranslation, @Nullable String bannerPath, @Nullable String overrideIconPath) {
        register(modID, nameTranslation, bannerPath, overrideIconPath, null, null);
    }

    /**
     * Registers a mod to CoreConfig with a banner, overrides the detected icon, and changes the colors of the UI when the mod is currently viewed.
     * @param modID The mod ID
     * @param nameTranslation The translation for the name in the config
     * @param bannerPath The path to an image for a banner to be displayed on the mod in the CoreConfig UI
     * @param overrideIconPath The path to an image for the detected icon to be overridden with in the CoreConfig UI
     * @param backgroundColor The ARGB background color for the mod
     * @param buttonColor The ARGB button color for the mod
     */
    static void register(String modID, String nameTranslation, @Nullable String bannerPath, @Nullable String overrideIconPath, @Nullable Integer backgroundColor, @Nullable Integer buttonColor) {
        ModHolder.addRegisteredMod(modID, nameTranslation, bannerPath, overrideIconPath, backgroundColor, buttonColor);
    }

}
