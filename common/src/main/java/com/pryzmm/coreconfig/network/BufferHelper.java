package com.pryzmm.coreconfig.network;

import com.pryzmm.coreconfig.CoreConfigConstants;
import net.minecraft.network.RegistryFriendlyByteBuf;
import java.util.ArrayList;
import java.util.List;

public class BufferHelper {

    public record Entry(String id, String path, Object value) {}

    public static void writeEntries(RegistryFriendlyByteBuf buf, List<Entry> entries) {
        buf.writeVarInt(entries.size());

        for (Entry entry : entries) {
            buf.writeUtf(entry.id());
            buf.writeUtf(entry.path());

            Object value = entry.value();

            switch (value) {
                case String s -> {
                    buf.writeByte(0);
                    buf.writeUtf(s);
                } case Integer i -> {
                    buf.writeByte(1);
                    buf.writeVarInt(i);
                } case Float f -> {
                    buf.writeByte(2);
                    buf.writeFloat(f);
                } case Double d -> {
                    buf.writeByte(3);
                    buf.writeDouble(d);
                } case Boolean b -> {
                    buf.writeByte(4);
                    buf.writeBoolean(b);
                } default -> throw new IllegalArgumentException("Unsupported type: " + value.getClass());
            }
        }
    }

    public static List<Entry> readEntries(RegistryFriendlyByteBuf buf) {
        int size = buf.readVarInt();
        List<Entry> list = new ArrayList<>(size);

        for (int i = 0; i < size; i++) {
            String id = buf.readUtf();
            String path = buf.readUtf();

            int type = buf.readByte();

            Object value = null;
            switch (type) {
                case 0 -> value = buf.readUtf();
                case 1 -> value = buf.readVarInt();
                case 2 -> value = buf.readFloat();
                case 3 -> value = buf.readDouble();
                case 4 -> value = buf.readBoolean();
                default -> CoreConfigConstants.LOGGER.error("Unknown type: {}", type);
            };
            if (value != null) list.add(new Entry(id, path, value));
        }

        return list;
    }

}
