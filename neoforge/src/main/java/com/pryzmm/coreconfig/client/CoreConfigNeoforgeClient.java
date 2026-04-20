package com.pryzmm.coreconfig.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.pryzmm.coreconfig.CoreConfigCommon;
import com.pryzmm.coreconfig.CoreConfigConstants;
import com.pryzmm.coreconfig.CoreConfigNeoforge;
import com.pryzmm.coreconfig.config.Config;
import com.pryzmm.coreconfig.services.Services;
import com.pryzmm.coreconfig.ui.CoreConfig;
import com.pryzmm.coreconfig.network.HostManager;
import com.pryzmm.coreconfig.ui.CoreConfigScreen;
import com.pryzmm.coreconfigapi.registrar.ConfigRegistrar;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import org.lwjgl.glfw.GLFW;

public class CoreConfigNeoforgeClient {

    public static ModContainer modContainer;

    public static KeyMapping OPEN_CONFIG;

    @EventBusSubscriber(modid = "coreconfig", value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            CoreConfigCommon.initFirst();
            modContainer.registerExtensionPoint(IConfigScreenFactory.class, (mc, screen) -> new CoreConfigScreen("coreconfig"));
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

    @EventBusSubscriber(modid = "coreconfig", value = Dist.CLIENT)
    public static class ClientGameEvents {

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
