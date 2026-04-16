package com.pryzmm.coreconfig.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import java.util.Map;
import java.util.stream.Collectors;

public record ServerSyncConfigPayload(String modID, String hostKey, Map<String, Object> values) implements CoreConfigPacket, CustomPacketPayload {

    public static final ResourceLocation ID = new ResourceLocation("coreconfig", "server_sync_config");

    public ServerSyncConfigPayload(FriendlyByteBuf buf) {
        this(buf.readUtf(), buf.readUtf(), BufferHelper.readEntries(buf).stream().collect(Collectors.toMap(BufferHelper.Entry::path, BufferHelper.Entry::value)));
    }

    @Override
    public void write(@NotNull FriendlyByteBuf buf) {
        buf.writeUtf(modID());
        buf.writeUtf(hostKey());
        BufferHelper.writeEntries(buf, values().entrySet().stream().map(e -> new BufferHelper.Entry(modID(), e.getKey(), e.getValue())).toList());
    }

    public static ServerSyncConfigPayload read(FriendlyByteBuf buf) {
        String modID = buf.readUtf();
        String hostKey = buf.readUtf();
        Map<String, Object> values = BufferHelper.readEntries(buf).stream().collect(Collectors.toMap(BufferHelper.Entry::path, BufferHelper.Entry::value));
        return new ServerSyncConfigPayload(modID, hostKey, values);
    }

    @Override
    public @NotNull ResourceLocation id() {
        return ID;
    }
}