package com.pryzmm.coreconfig;

import com.pryzmm.coreconfig.config.Config;
import com.pryzmm.coreconfig.network.ServerPacketCommon;
import com.pryzmm.coreconfig.services.Services;
import com.pryzmm.coreconfig.ui.CoreConfigScreen;
import com.pryzmm.coreconfig.network.HostManager;
import com.pryzmm.coreconfigapi.registrar.ConfigRegistrar;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;

@Mod("coreconfig")
@Mod.EventBusSubscriber(modid = "coreconfig", bus = Mod.EventBusSubscriber.Bus.FORGE) // Game bus for ServerStarting, PlayerLoggedIn
public class CoreConfigNeoforge {

    public static IEventBus eventBus;
    public static ModContainer modContainer;

    public CoreConfigNeoforge(IEventBus eventBus, ModContainer modContainer) {
        CoreConfigNeoforge.eventBus = eventBus;
        CoreConfigNeoforge.modContainer = modContainer;
    }

    @Mod.EventBusSubscriber(modid = "coreconfig", value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD) // Mod bus — FMLClientSetupEvent is IModBusEvent
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            CoreConfigCommon.initFirst();

            modContainer.registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory((minecraft, screen) -> new CoreConfigScreen("coreconfig"))
            );

            ConfigRegistrar.register(
                    CoreConfigConstants.MOD_ID,
                    "config.coreconfig.coreconfig",
                    "textures/config/banner.png",
                    "icon.png",
                    0x11CAF3FF,
                    0x3384BAFF
            );
            Config.register();
            CoreConfigCommon.init();
            Services.NETWORK.registerClientHandlers();
            Services.NETWORK.registerServerHandlers();
        }
    }

    @Mod.EventBusSubscriber(modid = "coreconfig", value = Dist.DEDICATED_SERVER, bus = Mod.EventBusSubscriber.Bus.MOD) // Mod bus — FMLDedicatedServerSetupEvent is IModBusEvent
    public static class ServerModEvents {

        @SubscribeEvent
        public static void onServerSetup(FMLDedicatedServerSetupEvent event) {
            CoreConfigCommon.initFirst();
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