package com.pryzmm.coreconfig;

import com.pryzmm.coreconfig.config.Config;
import com.pryzmm.coreconfig.data.ModHolder;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.Nullable;

public class CoreConfig implements ModInitializer {

    public static void register(Identifier modNameIdentifier) {
        ModHolder.addRegisteredMod(modNameIdentifier, null, null, null);
    }
    public static void register(Identifier modNameIdentifier, @Nullable Identifier modBannerIdentifier) {
        ModHolder.addRegisteredMod(modNameIdentifier, modBannerIdentifier, null, null);
    }
    public static void register(Identifier modNameIdentifier, @Nullable Identifier modBannerIdentifier, @Nullable Integer backgroundColor, @Nullable Integer buttonColor) {
        ModHolder.addRegisteredMod(modNameIdentifier, modBannerIdentifier, backgroundColor, buttonColor);
    }

    @Override
    public void onInitialize() {
        register(
            Identifier.fromNamespaceAndPath(CoreConfigConstants.MOD_ID, "config.coreconfig.coreconfig"),
            Identifier.fromNamespaceAndPath(CoreConfigConstants.MOD_ID, "textures/config/banner.png"),
            0x11CAF3FF,
            0x3384BAFF
        );
        Config.register();
    }

}
