package com.pryzmm.coreconfig.config;

import com.pryzmm.coreconfig.CoreConfigConstants;
import com.pryzmm.coreconfig.util.HostManager;
import com.pryzmm.coreconfigapi.Constants;
import com.pryzmm.coreconfigapi.data.ConfigType;
import com.pryzmm.coreconfigapi.entry.*;
import net.minecraft.network.chat.Component;

public class Config {

    public static WebsiteEntry githubButton = new WebsiteEntry.Builder(CoreConfigConstants.MOD_ID, "config.coreconfig.report_issues", "https://github.com/Pryzmm/CoreConfig")
        .hoverColor(0x551A1A1A)
        .priority(100)
        .build();

    public static DividerEntry visualsDivider = new DividerEntry.Builder(CoreConfigConstants.MOD_ID, "config.coreconfig.divider_visual")
        .priority(2)
        .textColor(0xFFFFFFFF)
        .build();

    public static BooleanEntry booleanUseSwitchTexture = new BooleanEntry.Builder(CoreConfigConstants.MOD_ID, "config.coreconfig.switch_texture", false)
        .descriptor(Component.translatable("config.coreconfig.switch_texture.desc"))
        .image("textures/config/switch_texture.png", 170, 43)
        .requiresRestart(false)
        .hoverColor(0x551A1A1A)
        .priority(2)
        .divider(visualsDivider)
        .type(ConfigType.SERVER)
        .build();

    public static DividerEntry debugDivider = new DividerEntry.Builder(CoreConfigConstants.MOD_ID, "config.coreconfig.divider_debug")
        .priority(1)
        .textColor(0xFFFFFFFF)
        .build();

    public static CustomEntry debugHost = new CustomEntry.Builder(CoreConfigConstants.MOD_ID, "config.coreconfig.debug_print_host")
        .runnable(() -> Constants.LOGGER.info("Current host of server (if server exists): {}", HostManager.getHostUUID()))
        .priority(3)
        .divider(debugDivider)
        .build();

    public static CustomEntry debugOverrideHost = new CustomEntry.Builder(CoreConfigConstants.MOD_ID, "config.coreconfig.debug_override_host")
        .runnable(() -> Constants.LOGGER.info("Current key: {}", HostManager.getHostKey()))
        .priority(2)
        .divider(debugDivider)
        .build();

    public static void register() {}

}
