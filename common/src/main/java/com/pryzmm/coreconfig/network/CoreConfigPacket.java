package com.pryzmm.coreconfig.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public interface CoreConfigPacket extends CustomPacketPayload {

    @NotNull ResourceLocation id();

    void write(@NotNull FriendlyByteBuf buf);

}
