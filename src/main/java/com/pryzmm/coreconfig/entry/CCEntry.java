package com.pryzmm.coreconfig.entry;

import com.pryzmm.coreconfig.ui.objects.ImageComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public interface CCEntry {

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