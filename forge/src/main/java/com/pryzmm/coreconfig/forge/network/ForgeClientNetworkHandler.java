package com.pryzmm.coreconfig.forge.network;

import com.pryzmm.coreconfig.network.CommonClientNetworkHandler;
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
        CommonClientNetworkHandler.receiveServerConfigPayload(payload);
    }

}