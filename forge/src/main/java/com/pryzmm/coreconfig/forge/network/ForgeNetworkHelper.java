package com.pryzmm.coreconfig.forge.network;

import com.pryzmm.coreconfig.CoreConfigConstants;
import com.pryzmm.coreconfig.network.INetworkHelper;
import com.pryzmm.coreconfig.network.ServerHostPayload;
import com.pryzmm.coreconfig.network.ServerSyncConfigPayload;
import com.pryzmm.coreconfig.util.Identifier;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.Channel;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.PacketDistributor;

public class ForgeNetworkHelper implements INetworkHelper {

    interface ClientHandler {
        void handleServerHost(ServerHostPayload payload);
        void handleServerSyncConfig(ServerSyncConfigPayload payload);
    }

    interface ServerHandler {
        void handleServerSyncConfigSent(ServerSyncConfigPayload payload, ServerPlayer sender);
    }

    private static Channel<CustomPacketPayload> channel;
    private static boolean registered;
    private static ClientHandler clientHandler;
    private static ServerHandler serverHandler;

    @Override
    public void registerPayloads() {
        if (registered) {
            return;
        }
        registered = true;

        channel = ChannelBuilder.named(Identifier.get(CoreConfigConstants.MOD_ID, "network"))
            .networkProtocolVersion(1)
            .payloadChannel()
            .play()
            .bidirectional()
            .addMain(
                ServerHostPayload.ID,
                ServerHostPayload.CODEC.cast(),
                ForgeNetworkHelper::handleServerHost
            )
            .addMain(
                ServerSyncConfigPayload.ID,
                ServerSyncConfigPayload.CODEC.cast(),
                ForgeNetworkHelper::handleServerSyncConfig
            )
            .build();
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
    public void sendToPlayers(MinecraftServer server, CustomPacketPayload payload) {
        if (channel == null) {
            return;
        }

        for (ServerPlayer player : server.getPlayerList().getPlayers()) {
            channel.send(payload, PacketDistributor.PLAYER.with(player));
        }
    }

    @Override
    public void sendToPlayer(ServerPlayer player, CustomPacketPayload payload) {
        if (channel == null) {
            return;
        }

        channel.send(payload, PacketDistributor.PLAYER.with(player));
    }

    @Override
    public void sendToServer(CustomPacketPayload payload) {
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
