package com.pryzmm.coreconfig.network;

import com.pryzmm.coreconfig.services.Services;
import com.pryzmm.coreconfigapi.Constants;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;

public class FabricServerNetworkHandler {

    private FabricServerNetworkHandler() {}

    static void register() {
        ServerPlayNetworking.registerGlobalReceiver(
            ServerSyncConfigPayload.ID,
            (payload, context) -> {
                Constants.LOGGER.info("Received config sync payload for mod {} with {} value(s), sending to all clients...", payload.modID(), payload.values().size());
                if (!payload.hostKey().equals(HostManager.getHostKey())) {
                    Constants.LOGGER.warn("Received server sync packet, but contained an invalid host key!");
                    return;
                }
                Services.NETWORK.sendToPlayers(HostManager.server, payload);
            }
        );
    }

}
