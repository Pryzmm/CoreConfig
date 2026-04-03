package com.pryzmm.coreconfigapi.data;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import java.util.*;

public class ModHolder {

    private static final HashMap<Identifier, ModData> configEntries = new HashMap<>();

    public static void addRegisteredMod(Identifier modNameIdentifier, Identifier modBannerIdentifier, Integer backgroundColor, Integer buttonColor) {
        ModData data = new ModData(modNameIdentifier, modBannerIdentifier, backgroundColor, buttonColor);
        configEntries.put(modNameIdentifier, data);
    }
    public static Collection<Identifier> getRegisteredMods(boolean sorted) {
        if (!sorted) return configEntries.keySet();
        else return configEntries.keySet().stream().sorted(Comparator.comparing(v -> Component.translatable(v.getPath()).getString())).toList();
    }
    public static Identifier getModByNamespace(String namespace) {
        return configEntries.keySet().stream().filter(id -> id.getNamespace().equals(namespace)).findFirst().orElse(null);
    }
    public static ModData getModData(String modID) {
        return configEntries.get(configEntries.keySet().stream().filter(id -> id.getNamespace().equals(modID)).findFirst().orElse(null));
    }

}
