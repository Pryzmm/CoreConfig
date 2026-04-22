package com.pryzmm.coreconfig.network;

import com.pryzmm.coreconfig.CoreConfigConstants;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class NeoforgeNetworkHelper implements INetworkHelper {

    interface ClientHandler {
        void handleServerHost(ServerHostPayload payload);
        void handleServerSyncConfig(ServerSyncConfigPayload payload);
    }

    interface ServerHandler {
        void handleServerSyncConfigSent(ServerSyncConfigPayload payload);
    }

    private static SimpleChannel channel;
    private static boolean registered;
    private static ClientHandler clientHandler;
    private static ServerHandler serverHandler;

    private static final String PROTOCOL_VERSION = "1";
    private static final ResourceLocation CHANNEL_ID =
            new ResourceLocation(CoreConfigConstants.MOD_ID, "network");

    @Override
    public void registerPayloads() {
        if (registered) return;
        registered = true;

        channel = NetworkRegistry.ChannelBuilder
            .named(CHANNEL_ID)
            .networkProtocolVersion(() -> PROTOCOL_VERSION)
            .clientAcceptedVersions(v -> true)
            .serverAcceptedVersions(v -> true)
            .simpleChannel();

        channel.messageBuilder(ServerHostPayload.class, 0)
            .encoder(ServerHostPayload::write)
            .decoder(ServerHostPayload::read)
            .consumerMainThread((payload, ctx) -> handleServerHost(payload, ctx.get()))
            .add();

        channel.messageBuilder(ServerSyncConfigPayload.class, 1)
            .encoder(ServerSyncConfigPayload::write)
            .decoder(ServerSyncConfigPayload::read)
            .consumerMainThread((payload, ctx) -> handleServerSyncConfig(payload, ctx.get()))
            .add();
    }

    @Override
    public void registerClientHandlers() {
        clientHandler = new NeoforgeClientNetworkHandler();
    }

    @Override
    public void registerServerHandlers() {
        serverHandler = new NeoforgeServerNetworkHandler();
    }

    @Override
    public void sendToPlayers(MinecraftServer server, CoreConfigPacket payload) {
        if (channel == null) return;
        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            channel.send(PacketDistributor.PLAYER.with(() -> player), payload);
        }
    }

    @Override
    public void sendToPlayer(ServerPlayer player, CoreConfigPacket payload) {
        if (channel == null) return;
        channel.send(PacketDistributor.PLAYER.with(() -> player), payload);
    }

    @Override
    public void sendToServer(CoreConfigPacket payload) {
        if (channel == null) return;
        channel.sendToServer(payload);
    }

    private static void handleServerHost(ServerHostPayload payload, NetworkEvent.Context ctx) {
        if (ctx.getDirection().getReceptionSide().isClient()) {
            ClientHandler handler = clientHandler;
            if (handler != null) handler.handleServerHost(payload);
        }
        ctx.setPacketHandled(true);
    }

    private static void handleServerSyncConfig(ServerSyncConfigPayload payload, NetworkEvent.Context ctx) {
        if (ctx.getDirection().getReceptionSide().isClient()) {
            ClientHandler handler = clientHandler;
            if (handler != null) handler.handleServerSyncConfig(payload);
        } else {
            ServerHandler handler = serverHandler;
            if (handler != null) handler.handleServerSyncConfigSent(payload);
        }
        ctx.setPacketHandled(true);
    }
}