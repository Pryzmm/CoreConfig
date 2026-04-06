package com.pryzmm.coreconfig;

import com.pryzmm.coreconfig.config.Config;
import com.pryzmm.coreconfigapi.registrar.ConfigRegistrar;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.Identifier;

public class CoreConfigFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        ConfigRegistrar.register(
            Identifier.fromNamespaceAndPath(CoreConfigConstants.MOD_ID, "config.coreconfig.coreconfig"),
            Identifier.fromNamespaceAndPath(CoreConfigConstants.MOD_ID, "textures/config/banner.png"),
            0x11CAF3FF,
            0x3384BAFF
        );
        Config.register();
    }
}
