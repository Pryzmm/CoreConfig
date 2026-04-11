package com.pryzmm.coreconfig.util;

import com.pryzmm.coreconfigapi.data.ModData;
import com.pryzmm.coreconfigapi.data.ModHolder;
import net.minecraft.network.chat.Component;
import java.util.Collection;
import java.util.Comparator;

public class ModHolderUtil {
    public static Collection<String> getRegisteredMods(boolean sorted) {
        if (!sorted) return ModHolder.getEntries().keySet();
        else return ModHolder.getEntries().keySet().stream().sorted(Comparator.comparing(v -> Component.translatable(v).getString())).toList();
    }
    public static ModData getModData(String modID) {
        return ModHolder.getEntries().get(ModHolder.getEntries().keySet().stream().filter(id -> id.equals(modID)).findFirst().orElse(null));
    }
}
