package com.pryzmm.coreconfigapi.registrar;

import com.pryzmm.coreconfigapi.data.ModHolder;
import org.jetbrains.annotations.Nullable;

public class ConfigRegistrar {

    public static void register(String modID, String nameTranslation) {
        register(modID, nameTranslation, null, null, null);
    }
    public static void register(String modID, String nameTranslation, @Nullable String bannerPath) {
        register(modID, nameTranslation, bannerPath, null, null);
    }
    public static void register(String modID, String nameTranslation, @Nullable String bannerPath, @Nullable Integer backgroundColor, @Nullable Integer buttonColor) {
        ModHolder.addRegisteredMod(modID, nameTranslation, bannerPath, backgroundColor, buttonColor);
    }

}
