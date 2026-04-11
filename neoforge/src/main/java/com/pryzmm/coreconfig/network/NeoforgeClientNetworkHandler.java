package com.pryzmm.coreconfig.network;

import com.pryzmm.coreconfig.util.HostManager;

public class NeoforgeClientNetworkHandler implements NeoforgeNetworkHelper.ClientHandler {

    @Override
    public void handleServerHost(ServerHostPayload payload) {
        HostManager.setHostUUID(payload.uuid());
    }

    @Override
    public void handleServerSyncConfig(ServerSyncConfigPayload payload) {
        CommonClientNetworkHandler.receiveServerConfigPayload(payload);
    }

}