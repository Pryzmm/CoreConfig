package com.pryzmm.coreconfig.forge.network;

import com.pryzmm.coreconfig.data.CCFileHandler;
import com.pryzmm.coreconfig.network.ServerHostPayload;
import com.pryzmm.coreconfig.network.ServerSyncConfigPayload;
import com.pryzmm.coreconfig.util.HostManager;

public class ForgeClientNetworkHandler implements ForgeNetworkHelper.ClientHandler {

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