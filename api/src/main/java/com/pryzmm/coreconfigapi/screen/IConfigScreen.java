package com.pryzmm.coreconfigapi.screen;

import com.pryzmm.coreconfigapi.entry.ColorEntry;

public interface IConfigScreen {

    void sendRestartPopup();
    void sendColorPopup(ColorEntry entry);

}
