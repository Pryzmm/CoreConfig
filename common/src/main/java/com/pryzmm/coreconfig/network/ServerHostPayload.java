package com.pryzmm.coreconfig.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import java.util.UUID;

public record ServerHostPayload(UUID uuid, String hostKey) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<@NotNull ServerHostPayload> ID = new CustomPacketPayload.Type<>(new ResourceLocation("coreconfig", "server_host"));

    public static final StreamCodec<@NotNull FriendlyByteBuf, @NotNull ServerHostPayload> CODEC = CustomPacketPayload.codec(ServerHostPayload::write, ServerHostPayload::read);

    private static void write(ServerHostPayload payload, FriendlyByteBuf buf) {
        if (payload.uuid != null) buf.writeUUID(payload.uuid());
        else buf.writeUUID(UUID.randomUUID());
        buf.writeUtf(payload.hostKey);
    }

    private static ServerHostPayload read(FriendlyByteBuf buf) {
        UUID uuid = buf.readUUID();
        String hostKey = buf.readUtf();
        return new ServerHostPayload(uuid, hostKey);
    }

    @Override
    public @NotNull Type<@NotNull ServerHostPayload> type() {
        return ID;
    }
}