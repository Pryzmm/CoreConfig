package com.pryzmm.coreconfig.network;

import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.Nullable;
import java.util.UUID;

public class HostManager {

    public static @Nullable MinecraftServer server = null;

    private static UUID hostUUID = null;
    private static String hostKey = null;

    public static void setHostUUID(UUID uuid) {
        if (hostUUID == null && server != null && !server.isDedicatedServer()) hostUUID = uuid;     // Server side
        if (server == null) hostUUID = uuid;                                                        // Client side
    }

    public static UUID getHostUUID() {
        return hostUUID;
    }

    public static void clear() {
        hostUUID = null;
        CommonClientNetworkHandler.clearServerConfig();
    }

    public static void setHostKey(String key) {
        hostKey = key;
    }

    public static String getHostKey() {
        return hostKey != null ? hostKey : "NotHost";
    }



}
