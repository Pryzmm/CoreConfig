package com.pryzmm.coreconfig.forge.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.pryzmm.coreconfig.CoreConfigConstants;
import com.pryzmm.coreconfig.services.Services;
import com.pryzmm.coreconfig.ui.CoreConfig;
import com.pryzmm.coreconfig.network.HostManager;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod("coreconfig")
public class CoreConfigForgeClient {

    public static KeyMapping OPEN_CONFIG;

    public CoreConfigForgeClient() {
        Services.NETWORK.registerClientHandlers();
        Services.NETWORK.registerServerHandlers();
    }

    @Mod.EventBusSubscriber(modid = "coreconfig", value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
            OPEN_CONFIG = new KeyMapping(
                    "key.coreconfig.open_config",
                    InputConstants.Type.KEYSYM,
                    GLFW.GLFW_KEY_INSERT,
                    new KeyMapping.Category(ResourceLocation.fromNamespaceAndPath(CoreConfigConstants.MOD_ID, "coreconfig"))
            );
            event.register(OPEN_CONFIG);
        }
    }

    @Mod.EventBusSubscriber(modid = "coreconfig", value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ClientForgeEvents {

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