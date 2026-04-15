package com.pryzmm.coreconfig.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class FabricNetworkHelper implements INetworkHelper {

    @Override
    public void registerPayloads() {
        PayloadTypeRegistry.clientboundPlay().register(
            ServerHostPayload.ID,
            ServerHostPayload.CODEC
        );
        PayloadTypeRegistry.clientboundPlay().register(
            ServerSyncConfigPayload.ID,
            ServerSyncConfigPayload.CODEC
        );
        PayloadTypeRegistry.serverboundPlay().register(
            ServerSyncConfigPayload.ID,
            ServerSyncConfigPayload.CODEC
        );
    }

    @Override
    public void registerClientHandlers() {
        FabricClientNetworkHandler.register();
    }

    @Override
    public void registerServerHandlers() {
        FabricServerNetworkHandler.register();
    }

    @Override
    public void sendToPlayers(MinecraftServer server, CustomPacketPayload payload) {
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            this.sendToPlayer(player, payload);
        }
    }

    @Override
    public void sendToPlayer(ServerPlayer player, CustomPacketPayload payload) {
        ServerPlayNetworking.send(player, payload);
    }

    @Override
    public void sendToServer(CustomPacketPayload payload) {
        ClientPlayNetworking.send(payload);
    }

}
