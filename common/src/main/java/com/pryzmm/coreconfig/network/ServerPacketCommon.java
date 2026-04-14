package com.pryzmm.coreconfig.network;

import com.pryzmm.coreconfig.data.EntryHolder;
import com.pryzmm.coreconfig.services.Services;
import com.pryzmm.coreconfig.util.ModHolderUtil;
import com.pryzmm.coreconfigapi.data.ConfigType;
import com.pryzmm.coreconfigapi.entry.*;
import net.minecraft.server.level.ServerPlayer;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class ServerPacketCommon {

    public static void pushPackets(ServerPlayer player) {
        HostManager.setHostUUID(player.getUUID());
        if (player.getUUID().equals(HostManager.getHostUUID())) {
            String AlphaNumericString = "abcdefghijklmnopqrstuvxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
            StringBuilder sb = new StringBuilder(64);
            for (int i = 0; i < 64; i++) {
                int index = (int)(AlphaNumericString.length() * Math.random());
                sb.append(AlphaNumericString.charAt(index));
            }
            HostManager.setHostKey(sb.toString());
            Services.NETWORK.sendToPlayer(player, new ServerHostPayload(HostManager.getHostUUID(), sb.toString()));
        }
        else Services.NETWORK.sendToPlayer(player, new ServerHostPayload(HostManager.getHostUUID(), "NotHost"));
        for (String modID : ModHolderUtil.getRegisteredMods(false)) {
            ServerSyncConfigPayload payload = getSyncConfigPayload(modID);
            Services.NETWORK.sendToPlayer(player, payload);
        }
    }

    public static ServerSyncConfigPayload getSyncConfigPayload(String modID) {
        Collection<CCEntry> entries = EntryHolder.get(modID).stream()
                .filter(v -> v instanceof MainEntry)
                .map(v -> (MainEntry) v)
                .filter(v -> v.type() != ConfigType.CLIENT)
                .map(v -> (CCEntry) v)
                .toList();
        Map<String, Object> values = new HashMap<>();
        for (CCEntry e : entries) {
            switch (e) {
                case BooleanEntry booleanEntry -> {
                    Boolean val = booleanEntry.getClientValue();
                    if (val != null) values.put(booleanEntry.translation(), val);
                }
                case StringEntry stringEntry -> {
                    String val = stringEntry.getClientValue();
                    if (val != null) values.put(stringEntry.translation(), val);
                }
                case IntegerEntry integerEntry -> {
                    Integer val = integerEntry.getClientValue();
                    if (val != null) values.put(integerEntry.translation(), val);
                }
                case DoubleEntry doubleEntry -> {
                    Double val = doubleEntry.getClientValue();
                    if (val != null) values.put(doubleEntry.translation(), val);
                }
                case FloatEntry floatEntry -> {
                    Float val = floatEntry.getClientValue();
                    if (val != null) values.put(floatEntry.translation(), val);
                }
                case LongEntry longEntry -> {
                    Long val = longEntry.getClientValue();
                    if (val != null) values.put(longEntry.translation(), val);
                }
                case ColorEntry colorEntry -> {
                    Integer val = colorEntry.getClientValue();
                    if (val != null) values.put(colorEntry.translation(), val);
                }
                case EnumEntry enumEntry -> {
                    Enum<?> val = enumEntry.getClientValue();
                    if (val != null) values.put(enumEntry.translation(), val.name());
                }
                default -> {}
            }
        }
        return new ServerSyncConfigPayload(modID, HostManager.getHostKey(), values);
    }

}
