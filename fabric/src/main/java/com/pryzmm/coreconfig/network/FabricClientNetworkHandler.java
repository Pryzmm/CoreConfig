package com.pryzmm.coreconfig.network;

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
            (payload, context) -> CommonClientNetworkHandler.receiveServerConfigPayload(payload)
        );
    }

}