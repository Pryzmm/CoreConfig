package com.pryzmm.coreconfig.config;

import com.pryzmm.coreconfig.CoreConfigConstants;
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

    public static BooleanEntry useSwitchTexture = new BooleanEntry.Builder(CoreConfigConstants.MOD_ID, "config.coreconfig.switch_texture", false)
        .descriptor(Component.translatable("config.coreconfig.switch_texture.desc"))
        .image("textures/config/switch_texture.png", 170, 43)
        .hoverColor(0x551A1A1A)
        .priority(2)
        .divider(visualsDivider)
        .build();

    public static ColorEntry lockedColor = new ColorEntry.Builder(CoreConfigConstants.MOD_ID, "config.coreconfig.locked_option", 0x44FF0011)
        .descriptor(Component.translatable("config.coreconfig.locked_option.desc"))
        .allowAlpha(true)
        .priority(1)
        .divider(visualsDivider)
        .build();

    public static DividerEntry tempDivider = new DividerEntry.Builder(CoreConfigConstants.MOD_ID, "Temporary Divider")
        .build();

    public static CustomEntry tempCustom = new CustomEntry.Builder(CoreConfigConstants.MOD_ID, "Temporary Custom")
        .divider(tempDivider)
        .build();

    public static DoubleEntry tempDouble = new DoubleEntry.Builder(CoreConfigConstants.MOD_ID, "Temporary Double", 0d)
        .divider(tempDivider)
        .build();

    public enum TempEnums {
        TEST1, test2, test_3
    }
    public static EnumEntry tempEnum = new EnumEntry.Builder(CoreConfigConstants.MOD_ID, "Temporary Enum", TempEnums.class, TempEnums.TEST1)
        .divider(tempDivider)
        .build();

    public static FloatEntry tempFloat = new FloatEntry.Builder(CoreConfigConstants.MOD_ID, "Temporary Float", 0f)
        .divider(tempDivider)
        .build();

    public static IntegerEntry tempInt = new IntegerEntry.Builder(CoreConfigConstants.MOD_ID, "Temporary Integer", 0)
        .divider(tempDivider)
        .build();

    public static LongEntry tempLong = new LongEntry.Builder(CoreConfigConstants.MOD_ID, "Temporary Long", 0L)
        .divider(tempDivider)
        .build();

    public static StringEntry tempString = new StringEntry.Builder(CoreConfigConstants.MOD_ID, "Temporary String", "")
        .divider(tempDivider)
        .build();

    public static WebsiteEntry tempWebsite = new WebsiteEntry.Builder(CoreConfigConstants.MOD_ID, "Temporary Website", "https://google.com/")
        .divider(tempDivider)
        .build();

    public static void register() {}

}
