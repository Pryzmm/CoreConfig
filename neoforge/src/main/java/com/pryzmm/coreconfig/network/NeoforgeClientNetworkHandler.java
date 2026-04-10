package com.pryzmm.coreconfig.network;

import com.pryzmm.coreconfig.data.CCFileHandler;
import com.pryzmm.coreconfig.util.HostManager;

public class NeoforgeClientNetworkHandler implements NeoforgeNetworkHelper.ClientHandler {

    @Override
    public void handleServerHost(ServerHostPayload payload) {
        HostManager.setHostUUID(payload.uuid());
    }

    @Override
    public void handleServerSyncConfig(ServerSyncConfigPayload payload) {
        System.out.println("Received config sync payload for mod " + payload.modID() + " with values: " + payload.values());
        CCFileHandler.saveServerSideConfig(payload.modID(), payload.values());
    }

}