package com.pryzmm.coreconfig;

import com.pryzmm.coreconfig.config.Config;
import com.pryzmm.coreconfigapi.registrar.ConfigRegistrar;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@Mod("coreconfig")
public class CoreConfigNeoforge {

    @EventBusSubscriber(modid = "coreconfig", value = Dist.CLIENT)
    public static class ModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {

            ConfigRegistrar.register(
                CoreConfigConstants.MOD_ID,
                "config.coreconfig.coreconfig",
                "textures/config/banner.png",
                0x11CAF3FF,
                0x3384BAFF
            );
            Config.register();

        }

    }
}
