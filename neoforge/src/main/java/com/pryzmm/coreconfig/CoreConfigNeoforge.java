package com.pryzmm.coreconfig;

import com.pryzmm.coreconfig.client.CoreConfigNeoforgeClient;
import com.pryzmm.coreconfig.network.ServerPacketCommon;
import com.pryzmm.coreconfig.services.Services;
import com.pryzmm.coreconfig.network.HostManager;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod("coreconfig")
@EventBusSubscriber(modid = "coreconfig")
public class CoreConfigNeoforge {

    public static IEventBus eventBus;

    public CoreConfigNeoforge(IEventBus eventBus, ModContainer modContainer) {
        CoreConfigNeoforge.eventBus = eventBus;
        if (FMLEnvironment.dist == Dist.CLIENT) CoreConfigNeoforgeClient.modContainer = modContainer;
    }

    @EventBusSubscriber(modid = "coreconfig", bus = EventBusSubscriber.Bus.MOD)
    public static class CommonModEvents {

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