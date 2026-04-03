package com.pryzmm.coreconfigapi.entry;

import com.pryzmm.coreconfigapi.component.ImageComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public interface MainEntry {

    Identifier identifier();
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

}