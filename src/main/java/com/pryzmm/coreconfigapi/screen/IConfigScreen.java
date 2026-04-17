package com.pryzmm.coreconfigapi.screen;

import com.pryzmm.coreconfigapi.entry.ColorEntry;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public interface IConfigScreen {

    @ApiStatus.Internal
    void sendRestartPopup();
    @ApiStatus.Internal
    void sendColorPopup(ColorEntry entry);

}
