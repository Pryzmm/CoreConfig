package com.pryzmm.coreconfig.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class FabricNetworkHelper implements INetworkHelper {

    @Override
    public void registerPayloads() {
        // 1.20.1 Fabric networking uses channel identifiers + raw FriendlyByteBuf.
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
    public void sendToPlayers(MinecraftServer server, CoreConfigPacket payload) {
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            this.sendToPlayer(player, payload);
        }
    }

    @Override
    public void sendToPlayer(ServerPlayer player, CoreConfigPacket payload) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        payload.write(buf);
        ServerPlayNetworking.send(player, payload.id(), buf);
    }

    @Override
    public void sendToServer(CoreConfigPacket payload) {
        FriendlyByteBuf buf = PacketByteBufs.create();
        payload.write(buf);
        ClientPlayNetworking.send(payload.id(), buf);
    }

}
