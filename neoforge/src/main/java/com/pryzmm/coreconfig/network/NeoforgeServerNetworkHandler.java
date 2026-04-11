package com.pryzmm.coreconfig.network;

import com.pryzmm.coreconfig.services.Services;
import com.pryzmm.coreconfigapi.Constants;

public class NeoforgeServerNetworkHandler implements NeoforgeNetworkHelper.ServerHandler {

    @Override
    public void handleServerSyncConfigSent(ServerSyncConfigPayload payload) {
        Constants.LOGGER.info("Received config sync payload for mod {} with {} value(s), sending to all clients...", payload.modID(), payload.values().size());
        if (!payload.hostKey().equals(HostManager.getHostKey())) {
            Constants.LOGGER.warn("Received server sync packet, but contained an invalid host key!");
            return;
        }
        Services.NETWORK.sendToPlayers(HostManager.server, payload);
    }

}