package com.pryzmm.coreconfig;

import com.pryzmm.coreconfig.config.Config;
import com.pryzmm.coreconfig.network.ServerPacketCommon;
import com.pryzmm.coreconfig.services.Services;
import com.pryzmm.coreconfig.ui.CoreConfigScreen;
import com.pryzmm.coreconfig.util.HostManager;
import com.pryzmm.coreconfigapi.registrar.ConfigRegistrar;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod("coreconfig")
@EventBusSubscriber(modid = "coreconfig")
public class CoreConfigNeoforge {

    public static IEventBus eventBus;

    public CoreConfigNeoforge(IEventBus eventBus, ModContainer modContainer) {
        CoreConfigNeoforge.eventBus = eventBus;
    }

    @EventBusSubscriber(modid = "coreconfig", value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent()
        public static void onClientSetup(FMLClientSetupEvent event) {

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

            Services.NETWORK.registerClientHandlers();
            Services.NETWORK.registerServerHandlers();

            event.getContainer().registerExtensionPoint(IConfigScreenFactory.class, (mc, screen) -> new CoreConfigScreen("coreconfig"));

        }

    }

    @EventBusSubscriber(modid = "coreconfig", value = Dist.DEDICATED_SERVER)
    public static class ServerModEvents {

        @SubscribeEvent()
        public static void onServerSetup(FMLDedicatedServerSetupEvent event) {
            CoreConfigCommon.init();
            Services.NETWORK.registerServerHandlers();
        }

    }

    @SubscribeEvent
    public static void onServerStarting(ServerStartingEvent event) {
        HostManager.server = event.getServer();
    }

    @SubscribeEvent
    public static void onPlayerJoin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (HostManager.server != null && HostManager.server.isSameThread()) ServerPacketCommon.pushPackets(player);
    }

}
