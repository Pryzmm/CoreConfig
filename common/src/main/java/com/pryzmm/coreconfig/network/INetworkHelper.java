package com.pryzmm.coreconfig.network;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public interface INetworkHelper {

    void registerPayloads();

    void registerClientHandlers();

    void registerServerHandlers();

    void sendToPlayers(MinecraftServer server, CustomPacketPayload payload);

    void sendToPlayer(ServerPlayer player, CustomPacketPayload payload);

    void sendToServer(CustomPacketPayload payload);

}