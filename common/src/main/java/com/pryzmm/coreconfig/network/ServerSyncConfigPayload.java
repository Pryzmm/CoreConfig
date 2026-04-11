package com.pryzmm.coreconfig.network;

import net.minecraft.resources.Identifier;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;
import java.util.Map;
import java.util.stream.Collectors;

public record ServerSyncConfigPayload(String modID, String hostKey, Map<String, Object> values) implements CustomPacketPayload {

    public static final Type<@NotNull ServerSyncConfigPayload> ID = new Type<>(Identifier.fromNamespaceAndPath("coreconfig", "server_sync_config"));

    public static final StreamCodec<@NotNull RegistryFriendlyByteBuf, @NotNull ServerSyncConfigPayload> CODEC = CustomPacketPayload.codec(ServerSyncConfigPayload::write, ServerSyncConfigPayload::read);

    private static void write(ServerSyncConfigPayload payload, RegistryFriendlyByteBuf buf) {
        buf.writeUtf(payload.modID());
        buf.writeUtf(payload.hostKey());
        BufferHelper.writeEntries(buf, payload.values.entrySet().stream().map(e -> new BufferHelper.Entry(payload.modID(), e.getKey(), e.getValue())).toList());
    }

    private static ServerSyncConfigPayload read(RegistryFriendlyByteBuf buf) {
        String modID = buf.readUtf();
        String hostKey = buf.readUtf();
        Map<String, Object> values = BufferHelper.readEntries(buf).stream().collect(Collectors.toMap(BufferHelper.Entry::path, BufferHelper.Entry::value));
        return new ServerSyncConfigPayload(modID, hostKey, values);
    }

    @Override
    public @NotNull Type<@NotNull ServerSyncConfigPayload> type() {
        return ID;
    }
}