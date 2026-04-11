package com.pryzmm.coreconfig.network;

import com.pryzmm.coreconfig.data.CCFileHandler;
import com.pryzmm.coreconfigapi.Constants;

public class CommonClientNetworkHandler {

    public static void receiveServerConfigPayload(ServerSyncConfigPayload payload) {
        Constants.LOGGER.info("Received config sync payload for mod {} with {} value(s)", payload.modID(), payload.values().size());
        CCFileHandler.saveServerSideConfig(payload.modID(), payload.values());
    }

    public static void clearServerConfig() {
        CCFileHandler.serverConfigs.clear();
    }

}
