package com.pryzmm.coreconfig.forge;

import com.pryzmm.coreconfig.CoreConfigCommon;
import com.pryzmm.coreconfig.CoreConfigConstants;
import com.pryzmm.coreconfig.config.Config;
import com.pryzmm.coreconfig.network.ServerPacketCommon;
import com.pryzmm.coreconfig.services.Services;
import com.pryzmm.coreconfig.network.HostManager;
import com.pryzmm.coreconfig.ui.CoreConfigScreen;
import com.pryzmm.coreconfigapi.registrar.ConfigRegistrar;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod("coreconfig")
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CoreConfigForge {

    @Mod.EventBusSubscriber(modid = "coreconfig", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

            ModLoadingContext.get().registerExtensionPoint(
                ConfigScreenHandler.ConfigScreenFactory.class,
                () -> new ConfigScreenHandler.ConfigScreenFactory((minecraft, screen) -> new CoreConfigScreen(CoreConfigConstants.MOD_ID))
            );

            CoreConfigCommon.initFirst();

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
