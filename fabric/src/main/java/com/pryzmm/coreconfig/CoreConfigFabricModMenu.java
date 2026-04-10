package com.pryzmm.coreconfig;

import com.pryzmm.coreconfig.ui.CoreConfigScreen;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class CoreConfigFabricModMenu implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> new CoreConfigScreen("coreconfig");
    }

}
