package com.pryzmm.coreconfig.network;

import com.pryzmm.coreconfig.data.CCFileHandler;
import com.pryzmm.coreconfig.util.HostManager;
import com.pryzmm.coreconfigapi.Constants;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;

@Environment(EnvType.CLIENT)
final class FabricClientNetworkHandler {

    private FabricClientNetworkHandler() {}

    static void register() {
        ClientPlayNetworking.registerGlobalReceiver(
            ServerHostPayload.ID,
            (payload, context) -> HostManager.setHostUUID(payload.uuid())
        );
        ClientPlayNetworking.registerGlobalReceiver(
            ServerSyncConfigPayload.ID,
            (payload, context) -> {
                Constants.LOGGER.info("Received config sync payload for mod {} with {} value(s)", payload.modID(), payload.values().size());
                CCFileHandler.saveServerSideConfig(payload.modID(), payload.values());
            }
        );
    }

}