package com.pryzmm.coreconfig.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.pryzmm.coreconfig.CoreConfigConstants;
import com.pryzmm.coreconfig.ui.CoreConfigScreen;
import com.pryzmm.coreconfig.util.Identifier;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.InputEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

@Mod("coreconfig")
public class CoreConfigNeoforgeClient {

    public static KeyMapping OPEN_CONFIG;

    @EventBusSubscriber(modid = "coreconfig", value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
            OPEN_CONFIG = new KeyMapping(
                "key.coreconfig.open_config",
                InputConstants.Type.KEYSYM,
                GLFW.GLFW_KEY_INSERT,
                new KeyMapping.Category(Identifier.get("coreconfig", "coreconfig"))
            );
            event.register(OPEN_CONFIG);
        }

        @SubscribeEvent
        public static void onKeyInput(InputEvent.Key event) {
            if (OPEN_CONFIG != null && OPEN_CONFIG.consumeClick()) {
                Minecraft client = Minecraft.getInstance();
                client.execute(() -> client.setScreen(new CoreConfigScreen(CoreConfigConstants.MOD_ID)));
            }
        }

    }

}
