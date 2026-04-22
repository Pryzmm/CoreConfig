package com.pryzmm.coreconfig.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public interface CoreConfigPacket extends Packet<PacketListener> {

    @NotNull ResourceLocation id();

    void write(@NotNull FriendlyByteBuf buf);

}
