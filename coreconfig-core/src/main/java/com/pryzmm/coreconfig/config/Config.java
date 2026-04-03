package com.pryzmm.coreconfig.config;

import com.pryzmm.coreconfig.CoreConfigConstants;
import com.pryzmm.coreconfigapi.entry.BooleanEntry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class Config {

    public static BooleanEntry booleanUseSwitchTexture = new BooleanEntry.Builder(Identifier.fromNamespaceAndPath(CoreConfigConstants.MOD_ID, "config.coreconfig.switch_texture"), false)
        .descriptor(Component.translatable("config.coreconfig.switch_texture.desc"))
        .image(Identifier.fromNamespaceAndPath(CoreConfigConstants.MOD_ID, "textures/config/switch_texture.png"), 170, 43)
        .requiresRestart(false)
        .hoverColor(0x551A1A1A)
        .priority(-1)
        .build();

//    public static final CCEntry<Boolean> useSwitchTexture = CCEntry.of(
//        () -> new BooleanEntry.Builder(
//            Identifier.fromNamespaceAndPath(CoreConfigConstants.MOD_ID, "config.coreconfig.switch_texture"), false)
//            .descriptor(Component.translatable("config.coreconfig.switch_texture.desc"))
//            .requiresRestart(false)
//            .build(),
//        false
//    );

    public static void register() {}

}
