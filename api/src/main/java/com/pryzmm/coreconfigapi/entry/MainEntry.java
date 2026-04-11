package com.pryzmm.coreconfigapi.entry;

import com.pryzmm.coreconfigapi.component.ImageComponent;
import com.pryzmm.coreconfigapi.data.ConfigType;
import org.jetbrains.annotations.ApiStatus;

/**
 * An interface to attach all main entries together
 */
@ApiStatus.Internal
public interface MainEntry extends CCEntry {

    String translation();
    String modID();
    void save();
    Object getUnsavedValue();
    Object getDefaultValue();
    Object getClientValue();
    Object getServerValue();
    Object getValue();
    int priority();
    boolean requiresRestart();
    Integer hoverColor();
    Object descriptor();
    void refreshValue();
    ImageComponent image();
    DividerEntry divider();
    ConfigType type();

}