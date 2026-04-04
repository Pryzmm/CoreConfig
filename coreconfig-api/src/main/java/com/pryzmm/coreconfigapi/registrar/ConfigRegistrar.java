package com.pryzmm.coreconfigapi.registrar;

import com.pryzmm.coreconfigapi.data.ModHolder;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

public class ConfigRegistrar {

    public static void register(Identifier modNameIdentifier) {
        register(modNameIdentifier, null, null, null);
    }
    public static void register(Identifier modNameIdentifier, @Nullable Identifier modBannerIdentifier) {
        register(modNameIdentifier, modBannerIdentifier, null, null);
    }
    public static void register(Identifier modNameIdentifier, @Nullable Identifier modBannerIdentifier, @Nullable Integer backgroundColor, @Nullable Integer buttonColor) {
        ModHolder.addRegisteredMod(modNameIdentifier, modBannerIdentifier, backgroundColor, buttonColor);
    }

}
