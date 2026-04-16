package com.pryzmm.coreconfig.network;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public interface INetworkHelper {

    void registerPayloads();

    void registerClientHandlers();

    void registerServerHandlers();

    void sendToPlayers(MinecraftServer server, CoreConfigPacket payload);

    void sendToPlayer(ServerPlayer player, CoreConfigPacket payload);

    void sendToServer(CoreConfigPacket payload);

}