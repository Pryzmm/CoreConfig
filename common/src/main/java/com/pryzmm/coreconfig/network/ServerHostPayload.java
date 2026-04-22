package com.pryzmm.coreconfig.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import java.util.UUID;

public record ServerHostPayload(UUID uuid, String hostKey) implements CoreConfigPacket, Packet<PacketListener> {

    public static final ResourceLocation ID = new ResourceLocation("coreconfig", "server_host");

    public ServerHostPayload(FriendlyByteBuf buf) {
        this(buf.readUUID(), buf.readUtf());
    }

    public void write(@NotNull FriendlyByteBuf buf) {
        if (uuid() != null) buf.writeUUID(uuid());
        else buf.writeUUID(UUID.randomUUID());
        buf.writeUtf(hostKey());
    }

    @Override
    public void handle(@NotNull PacketListener packetListener) {}

    public static ServerHostPayload read(FriendlyByteBuf buf) {
        UUID uuid = buf.readUUID();
        String hostKey = buf.readUtf();
        return new ServerHostPayload(uuid, hostKey);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }
}