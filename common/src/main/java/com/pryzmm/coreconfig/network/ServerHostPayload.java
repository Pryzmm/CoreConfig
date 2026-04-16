package com.pryzmm.coreconfig.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import java.util.UUID;

public record ServerHostPayload(UUID uuid, String hostKey) implements CoreConfigPacket, CustomPacketPayload {

    public static final ResourceLocation ID = new ResourceLocation("coreconfig", "server_host");

    public ServerHostPayload(FriendlyByteBuf buf) {
        this(buf.readUUID(), buf.readUtf());
    }

    public void write(@NotNull FriendlyByteBuf buf) {
        if (uuid() != null) buf.writeUUID(uuid());
        else buf.writeUUID(UUID.randomUUID());
        buf.writeUtf(hostKey());
    }

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