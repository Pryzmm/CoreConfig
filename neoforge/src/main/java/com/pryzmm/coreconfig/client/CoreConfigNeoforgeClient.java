package com.pryzmm.coreconfig.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.pryzmm.coreconfig.CoreConfigConstants;
import com.pryzmm.coreconfig.ui.CoreConfig;
import com.pryzmm.coreconfig.network.HostManager;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.lwjgl.glfw.GLFW;

public class CoreConfigNeoforgeClient {

    public CoreConfigNeoforgeClient(ModContainer container) {}

    public static KeyMapping OPEN_CONFIG;

    @EventBusSubscriber(modid = "coreconfig", value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
            OPEN_CONFIG = new KeyMapping(
                "key.coreconfig.open_config",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_INSERT,
                "config.coreconfig.coreconfig"
            );
            event.register(OPEN_CONFIG);
        }

    }

    @EventBusSubscriber(modid = "coreconfig", value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
    public static class ClientGameModEvents {

        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if (OPEN_CONFIG != null && OPEN_CONFIG.consumeClick()) {
                new CoreConfig().open(CoreConfigConstants.MOD_ID);
            }
        }

        @SubscribeEvent
        public static void onPlayerQuit(PlayerEvent.PlayerLoggedOutEvent event) {
            HostManager.clear();
        }

    }

}
