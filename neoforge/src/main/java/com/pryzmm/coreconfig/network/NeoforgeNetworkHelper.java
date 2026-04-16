package com.pryzmm.coreconfig.network;

import com.pryzmm.coreconfig.CoreConfigNeoforge;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;

public class NeoforgeNetworkHelper implements INetworkHelper {

    interface ClientHandler {
        void handleServerHost(ServerHostPayload payload);
        void handleServerSyncConfig(ServerSyncConfigPayload payload);
    }

    interface ServerHandler {
        void handleServerSyncConfigSent(ServerSyncConfigPayload payload);
    }

    private static boolean registered;
    private static ClientHandler clientHandler;
    private static ServerHandler serverHandler;

    @Override
    public void registerPayloads() {
        if (registered) {
            return;
        }
        registered = true;
        CoreConfigNeoforge.eventBus.addListener(NeoforgeNetworkHelper::onRegisterPayloads);
    }

    private static void onRegisterPayloads(RegisterPayloadHandlerEvent event) {
        IPayloadRegistrar registrar = event.registrar("coreconfig");
        registrar.play(
            ServerHostPayload.ID,
            ServerHostPayload::new,
            handler -> handler.client(NeoforgeNetworkHelper::handleServerHost)
        );
        registrar.play(
            ServerSyncConfigPayload.ID,
            ServerSyncConfigPayload::new,
            handler -> handler
            .client(NeoforgeNetworkHelper::handleServerSyncConfig)
            .server(NeoforgeNetworkHelper::handleServerSyncConfig)
        );
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
        PacketDistributor.ALL.noArg().send(payload);
    }

    @Override
    public void sendToPlayer(ServerPlayer player, CoreConfigPacket payload) {
        PacketDistributor.PLAYER.with(player).send(payload);
    }

    @Override
    public void sendToServer(CoreConfigPacket payload) {
        ClientPacketListener connection = Minecraft.getInstance().getConnection();
        if (connection != null) connection.send(payload);
    }

    private static void handleServerHost(ServerHostPayload payload, IPayloadContext context) {
        context.workHandler().execute(() -> {
            ClientHandler handler = clientHandler;
            if (handler != null) {
                handler.handleServerHost(payload);
            }
        });
    }

    private static void handleServerSyncConfig(ServerSyncConfigPayload payload, IPayloadContext context) {
        context.workHandler().execute(() -> {
            if (context.flow() == PacketFlow.CLIENTBOUND) {
                ClientHandler handler = clientHandler;
                if (handler != null) {
                    handler.handleServerSyncConfig(payload);
                }
            } else {
                ServerHandler handler = serverHandler;
                if (handler != null) {
                    handler.handleServerSyncConfigSent(payload);
                }
            }
        });
    }

}
