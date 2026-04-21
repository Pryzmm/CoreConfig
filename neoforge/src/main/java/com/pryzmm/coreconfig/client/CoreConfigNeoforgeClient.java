package com.pryzmm.coreconfig.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.pryzmm.coreconfig.CoreConfigConstants;
import com.pryzmm.coreconfig.ui.CoreConfig;
import com.pryzmm.coreconfig.network.HostManager;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

public class CoreConfigNeoforgeClient {

    public CoreConfigNeoforgeClient(ModContainer container) {}

    public static KeyMapping OPEN_CONFIG;

    @Mod.EventBusSubscriber(modid = "coreconfig", value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
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

    @Mod.EventBusSubscriber(modid = "coreconfig", value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
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
