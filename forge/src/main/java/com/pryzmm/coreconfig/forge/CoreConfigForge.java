package com.pryzmm.coreconfig.forge;

import com.pryzmm.coreconfig.CoreConfigConstants;
import com.pryzmm.coreconfig.config.Config;
import com.pryzmm.coreconfigapi.registrar.ConfigRegistrar;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod("coreconfig")
public class CoreConfigForge {

    @Mod.EventBusSubscriber(modid = "coreconfig", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

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
