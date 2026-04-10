package com.pryzmm.coreconfigapi.data;

import net.minecraft.network.chat.Component;
import java.util.*;

public class ModHolder {

    private static final HashMap<String, ModData> configEntries = new HashMap<>();

    public static void addRegisteredMod(String modID, String nameTranslation, String bannerPath, String overrideIconPath, Integer backgroundColor, Integer buttonColor) {
        ModData data = new ModData(modID, nameTranslation, bannerPath, overrideIconPath, backgroundColor, buttonColor);
        configEntries.put(modID, data);
    }
    public static Collection<String> getRegisteredMods(boolean sorted) {
        if (!sorted) return configEntries.keySet();
        else return configEntries.keySet().stream().sorted(Comparator.comparing(v -> Component.translatable(v).getString())).toList();
    }
    public static ModData getModData(String modID) {
        return configEntries.get(configEntries.keySet().stream().filter(id -> id.equals(modID)).findFirst().orElse(null));
    }

}
