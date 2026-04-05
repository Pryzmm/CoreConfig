package com.pryzmm.coreconfig.config;

import com.pryzmm.coreconfig.CoreConfigConstants;
import com.pryzmm.coreconfigapi.entry.*;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class Config {

    public static WebsiteEntry githubButton = new WebsiteEntry.Builder(Identifier.fromNamespaceAndPath(CoreConfigConstants.MOD_ID, "config.coreconfig.report_issues"), "https://github.com/Pryzmm/CoreConfig")
        .hoverColor(0x551A1A1A)
        .priority(100)
        .build();

    public static DividerEntry divider = new DividerEntry.Builder(Identifier.fromNamespaceAndPath(CoreConfigConstants.MOD_ID, "config.coreconfig.divider_visual"))
        .priority(0)
        .textColor(0xFFFFFFFF)
        .build();

    public static BooleanEntry booleanUseSwitchTexture = new BooleanEntry.Builder(Identifier.fromNamespaceAndPath(CoreConfigConstants.MOD_ID, "config.coreconfig.switch_texture"), false)
        .descriptor(Component.translatable("config.coreconfig.switch_texture.desc"))
        .image(Identifier.fromNamespaceAndPath(CoreConfigConstants.MOD_ID, "textures/config/switch_texture.png"), 170, 43)
        .requiresRestart(false)
        .hoverColor(0x551A1A1A)
        .priority(2)
        .divider(divider)
        .build();

    public static ColorEntry colorColor = new ColorEntry.Builder(Identifier.fromNamespaceAndPath(CoreConfigConstants.MOD_ID, "config.coreconfig.color"), 0x33FF0000)
        .requiresRestart(false)
        .hoverColor(0x551A1A1A)
        .priority(1)
        .divider(divider)
        .allowAlpha(true)
        .build();

    public static CustomEntry customEntry = new CustomEntry.Builder(Identifier.fromNamespaceAndPath(CoreConfigConstants.MOD_ID, "config.coreconfig.color"))
        .runnable(() -> System.out.println("Custom entry clicked!"))
        .hoverColor(0x551A1A1A)
        .priority(0)
        .divider(divider)
        .build();

    public static void register() {}

}
