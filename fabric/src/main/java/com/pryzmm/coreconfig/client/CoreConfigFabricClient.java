package com.pryzmm.coreconfig.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.pryzmm.coreconfig.CoreConfigConstants;
import com.pryzmm.coreconfig.services.Services;
import com.pryzmm.coreconfig.ui.CoreConfig;
import com.pryzmm.coreconfig.network.HostManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.KeyMapping;
import org.lwjgl.glfw.GLFW;

public class CoreConfigFabricClient implements ClientModInitializer {

    public static KeyMapping OPEN_CONFIG;

    @Override
    public void onInitializeClient() {

        Services.NETWORK.registerClientHandlers();

        OPEN_CONFIG = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.coreconfig.open_config",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_INSERT,
            "config.coreconfig.coreconfig")
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (OPEN_CONFIG.consumeClick()) new CoreConfig().open(CoreConfigConstants.MOD_ID);
        });

        ClientPlayConnectionEvents.DISCONNECT.register((listener, minecraft) -> HostManager.clear());

    }
}
