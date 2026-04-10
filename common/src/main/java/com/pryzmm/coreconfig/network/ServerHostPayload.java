package com.pryzmm.coreconfig.network;

import com.pryzmm.coreconfig.util.Identifier;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;
import java.util.UUID;

public record ServerHostPayload(UUID uuid, String hostKey) implements CustomPacketPayload {

    public static final CustomPacketPayload.Type<@NotNull ServerHostPayload> ID = new CustomPacketPayload.Type<>(Identifier.get("coreconfig", "server_host"));

    public static final StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull ServerHostPayload> CODEC = CustomPacketPayload.codec(ServerHostPayload::write, ServerHostPayload::read);

    private static void write(ServerHostPayload payload, RegistryFriendlyByteBuf buf) {
        if (payload.uuid != null) buf.writeUUID(payload.uuid());
        else buf.writeUUID(UUID.randomUUID());
        buf.writeUtf(payload.hostKey);
    }

    private static ServerHostPayload read(RegistryFriendlyByteBuf buf) {
        UUID uuid = buf.readUUID();
        String hostKey = buf.readUtf();
        return new ServerHostPayload(uuid, hostKey);
    }

    @Override
    public @NotNull Type<@NotNull ServerHostPayload> type() {
        return ID;
    }
}