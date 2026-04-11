package com.pryzmm.coreconfig;

import com.pryzmm.coreconfig.config.Config;
import com.pryzmm.coreconfig.network.ServerPacketCommon;
import com.pryzmm.coreconfig.services.Services;
import com.pryzmm.coreconfig.network.HostManager;
import com.pryzmm.coreconfigapi.registrar.ConfigRegistrar;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class CoreConfigFabric implements ModInitializer {

    @Override
    public void onInitialize() {
        CoreConfigCommon.initFirst();

        ConfigRegistrar.register(
            CoreConfigConstants.MOD_ID,
            "config.coreconfig.coreconfig",
            "textures/config/banner.png",
            "textures/config/icon.png",
            0x11CAF3FF,
            0x3384BAFF
        );
        Config.register();
        CoreConfigCommon.init();

        Services.NETWORK.registerServerHandlers();

        ServerLifecycleEvents.SERVER_STARTING.register(server -> HostManager.server = server);

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            if (HostManager.server != null && HostManager.server.isSameThread()) ServerPacketCommon.pushPackets(handler.player);
        });

    }
}
