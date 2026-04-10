package com.pryzmm.coreconfig.ui;

import com.pryzmm.coreconfigapi.screen.ConfigScreen;
import net.minecraft.client.Minecraft;

public class CoreConfig extends ConfigScreen {

    @Override
    public void open(String modID) {
        Minecraft client = Minecraft.getInstance();
        client.execute(() -> client.setScreen(new CoreConfigScreen(modID)));
    }

}
