package com.pryzmm.coreconfig.forge.network;

import com.pryzmm.coreconfig.network.ServerSyncConfigPayload;
import com.pryzmm.coreconfig.services.Services;
import com.pryzmm.coreconfig.network.HostManager;
import com.pryzmm.coreconfigapi.Constants;
import net.minecraft.server.level.ServerPlayer;

public class ForgeServerNetworkHandler implements ForgeNetworkHelper.ServerHandler {

    @Override
    public void handleServerSyncConfigSent(ServerSyncConfigPayload payload, ServerPlayer sender) {
        if (sender.getUUID().equals(HostManager.getHostUUID())) {
            Constants.LOGGER.info("Received config sync payload for mod {} with {} value(s), sending to all clients...", payload.modID(), payload.values().size());
            if (!payload.hostKey().equals(HostManager.getHostKey())) {
                Constants.LOGGER.warn("Received server sync packet, but contained an invalid host key!");
                return;
            }
            Services.NETWORK.sendToPlayers(HostManager.server, payload);
        }
    }

}