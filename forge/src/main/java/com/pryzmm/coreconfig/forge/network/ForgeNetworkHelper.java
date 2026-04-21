package com.pryzmm.coreconfig.forge.network;

import com.pryzmm.coreconfig.CoreConfigConstants;
import com.pryzmm.coreconfig.network.CoreConfigPacket;
import com.pryzmm.coreconfig.network.INetworkHelper;
import com.pryzmm.coreconfig.network.ServerHostPayload;
import com.pryzmm.coreconfig.network.ServerSyncConfigPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.*;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Supplier;

@SuppressWarnings("removal")
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

    private static final Supplier<String> PROTOCOL_VERSION = () -> "1";
    private static final ResourceLocation CHANNEL_ID = new ResourceLocation(CoreConfigConstants.MOD_ID, "network");

    @Override
    public void registerPayloads() {
        if (registered) return;
        registered = true;

        channel = NetworkRegistry.ChannelBuilder
            .named(CHANNEL_ID)
            .networkProtocolVersion(PROTOCOL_VERSION)
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
            channel.send(PacketDistributor.PLAYER.with(() -> player), payload);
        }
    }

    @Override
    public void sendToPlayer(ServerPlayer player, CoreConfigPacket payload) {
        if (channel == null) {
            return;
        }

        channel.send( PacketDistributor.PLAYER.with(() -> player), payload);
    }

    @Override
    public void sendToServer(CoreConfigPacket payload) {
        if (channel == null) {
            return;
        }
        channel.send(PacketDistributor.SERVER.noArg(), payload);

    }

    private static void handleServerHost(ServerHostPayload payload, NetworkEvent.Context context) {
        if (context.getDirection() != NetworkDirection.PLAY_TO_CLIENT) {
            return;
        }

        ClientHandler handler = clientHandler;
        if (handler != null) {
            handler.handleServerHost(payload);
        }
    }

    private static void handleServerSyncConfig(ServerSyncConfigPayload payload, NetworkEvent.Context context) {
        if (context.getDirection() != NetworkDirection.PLAY_TO_CLIENT) {
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
