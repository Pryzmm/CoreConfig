package com.pryzmm.coreconfig.forge.network;

import com.pryzmm.coreconfig.CoreConfigConstants;
import com.pryzmm.coreconfig.network.CoreConfigPacket;
import com.pryzmm.coreconfig.network.INetworkHelper;
import com.pryzmm.coreconfig.network.ServerHostPayload;
import com.pryzmm.coreconfig.network.ServerSyncConfigPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.*;

public class ForgeNetworkHelper implements INetworkHelper {

    interface ClientHandler {
        void handleServerHost(ServerHostPayload payload);
        void handleServerSyncConfig(ServerSyncConfigPayload payload);
    }

    interface ServerHandler {
        void handleServerSyncConfigSent(ServerSyncConfigPayload payload, ServerPlayer sender);
    }

    private static SimpleChannel channel;
    private static boolean registered;
    private static ClientHandler clientHandler;
    private static ServerHandler serverHandler;

    private static final int PROTOCOL_VERSION = 1;
    private static final ResourceLocation CHANNEL_ID = new ResourceLocation(CoreConfigConstants.MOD_ID, "network");

    @Override
    public void registerPayloads() {
        if (registered) return;
        registered = true;

        channel = ChannelBuilder
                .named(CHANNEL_ID)
                .networkProtocolVersion(PROTOCOL_VERSION)
                .clientAcceptedVersions(Channel.VersionTest.exact(1))
                .serverAcceptedVersions(Channel.VersionTest.exact(1))
                .simpleChannel();

        channel.messageBuilder(ServerHostPayload.class)
                .encoder(ServerHostPayload::write)
                .decoder(ServerHostPayload::read)
                .consumerMainThread(ForgeNetworkHelper::handleServerHost)
                .add();

        channel.messageBuilder(ServerSyncConfigPayload.class)
                .encoder(ServerSyncConfigPayload::write)
                .decoder(ServerSyncConfigPayload::read)
                .consumerMainThread(ForgeNetworkHelper::handleServerSyncConfig)
                .add();

    }

    @Override
    public void registerClientHandlers() {
        if (clientHandler == null) clientHandler = new ForgeClientNetworkHandler();
    }

    @Override
    public void registerServerHandlers() {
        if (serverHandler == null) serverHandler = new ForgeServerNetworkHandler();
    }

    @Override
    public void sendToPlayers(MinecraftServer server, CoreConfigPacket payload) {
        if (channel == null) {
            return;
        }

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            channel.send(payload, PacketDistributor.PLAYER.with(player));
        }
    }

    @Override
    public void sendToPlayer(ServerPlayer player, CoreConfigPacket payload) {
        if (channel == null) {
            return;
        }

        channel.send(payload, PacketDistributor.PLAYER.with(player));
    }

    @Override
    public void sendToServer(CoreConfigPacket payload) {
        if (channel == null) {
            return;
        }
        channel.send(payload, PacketDistributor.SERVER.noArg());

    }

    private static void handleServerHost(ServerHostPayload payload, CustomPayloadEvent.Context context) {
        if (!context.isClientSide()) {
            return;
        }

        ClientHandler handler = clientHandler;
        if (handler != null) {
            handler.handleServerHost(payload);
        }
    }

    private static void handleServerSyncConfig(ServerSyncConfigPayload payload, CustomPayloadEvent.Context context) {
        if (context.isClientSide()) {
            ClientHandler handler = clientHandler;
            if (handler != null) {
                handler.handleServerSyncConfig(payload);
            }
        } else {
            ServerHandler handler = serverHandler;
            if (handler != null) {
                handler.handleServerSyncConfigSent(payload, context.getSender());
            }
        }
    }

}