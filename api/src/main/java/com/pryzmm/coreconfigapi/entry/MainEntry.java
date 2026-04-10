package com.pryzmm.coreconfigapi.entry;

import com.pryzmm.coreconfigapi.component.ImageComponent;
import com.pryzmm.coreconfigapi.data.ConfigType;
import net.minecraft.network.chat.Component;

public interface MainEntry extends CCEntry {

    String translation();
    String modID();
    void save();
    Object getUnsavedValue();
    Object getDefaultValue();
    Object getValue();
    int priority();
    boolean requiresRestart();
    Integer hoverColor();
    Component descriptor();
    void refreshValue();
    ImageComponent image();
    DividerEntry divider();
    ConfigType type();

}