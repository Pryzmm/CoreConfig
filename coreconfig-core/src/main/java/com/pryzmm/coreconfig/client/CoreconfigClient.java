package com.pryzmm.coreconfig.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.pryzmm.coreconfig.CoreConfigConstants;
import com.pryzmm.coreconfigapi.entry.MainEntry;
import com.pryzmm.coreconfig.ui.CoreConfigScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;

public class CoreconfigClient implements ClientModInitializer {

    public static KeyMapping OPEN_CONFIG;

    public static MainEntry hoveredEntry = null;

    @Override
    public void onInitializeClient() {

        OPEN_CONFIG = KeyMappingHelper.registerKeyMapping(new KeyMapping(
            "key.coreconfig.open_config",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_INSERT,
            new KeyMapping.Category(Identifier.fromNamespaceAndPath("coreconfig", "coreconfig"))
        ));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (OPEN_CONFIG.consumeClick()) client.execute(() -> client.setScreen(new CoreConfigScreen(CoreConfigConstants.MOD_ID)));
        });

    }
}
