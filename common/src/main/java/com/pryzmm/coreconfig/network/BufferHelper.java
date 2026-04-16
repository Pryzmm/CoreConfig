package com.pryzmm.coreconfig.network;

import com.pryzmm.coreconfig.CoreConfigConstants;
import net.minecraft.network.FriendlyByteBuf;
import java.util.ArrayList;
import java.util.List;

public class BufferHelper {

    public record Entry(String id, String path, Object value) {}

    public static void writeEntries(FriendlyByteBuf buf, List<Entry> entries) {
        buf.writeVarInt(entries.size());

        for (Entry entry : entries) {
            buf.writeUtf(entry.id());
            buf.writeUtf(entry.path());

            Object value = entry.value();

            if (value instanceof String s) {
                buf.writeByte(0);
                buf.writeUtf(s);
            } else if (value instanceof Integer i) {
                buf.writeByte(1);
                buf.writeVarInt(i);
            } else if (value instanceof Float f) {
                buf.writeByte(2);
                buf.writeFloat(f);
            } else if (value instanceof Double d) {
                buf.writeByte(3);
                buf.writeDouble(d);
            } else if (value instanceof Boolean b) {
                buf.writeByte(4);
                buf.writeBoolean(b);
            } else if (value instanceof Long l) {
                buf.writeByte(5);
                buf.writeLong(l);
            } else throw new IllegalArgumentException("Unsupported type: " + value.getClass());
        }
    }

    public static List<Entry> readEntries(FriendlyByteBuf buf) {
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
                case 5 -> value = buf.readLong();
                default -> CoreConfigConstants.LOGGER.error("Unknown type: {}", type);
            }
            if (value != null) list.add(new Entry(id, path, value));
        }

        return list;
    }

}
