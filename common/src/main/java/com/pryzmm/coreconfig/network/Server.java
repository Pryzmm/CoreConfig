package com.pryzmm.coreconfig.network;

import net.minecraft.client.Minecraft;

public class Server {

    public static boolean isHostingServer() {
        if (HostManager.server == null) return false;
        return (Minecraft.getInstance().player != null && Minecraft.getInstance().player.getUUID().equals(HostManager.getHostUUID()));
    }

}
