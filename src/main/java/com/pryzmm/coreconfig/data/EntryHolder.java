package com.pryzmm.coreconfig.data;

import com.pryzmm.coreconfig.entry.CCEntry;
import com.pryzmm.coreconfig.entry.IntegerEntry;
import com.pryzmm.coreconfig.entry.StringEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class EntryHolder {

    private static final HashMap<String, ArrayList<CCEntry>> configEntries = new HashMap<>();

    public static void addEntry(String modID, CCEntry entry) {
        configEntries.computeIfAbsent(modID, _ -> new ArrayList<>()).add(entry);
    }

    public static Collection<CCEntry> get(String modID) {
        return configEntries.getOrDefault(modID, new ArrayList<>()).stream().sorted((e1, e2) -> Integer.compare(e2.priority(), e1.priority())).toList();
    }

    public static boolean isInvalidConfig(String modID) {
        if (configEntries.containsKey(modID)) {
            for (CCEntry entry : configEntries.get(modID)) {
                if (entry instanceof StringEntry stringEntry) {
                    String value = stringEntry.getUnsavedValue();
                    if (value.length() < stringEntry.minimumLength() || value.length() > stringEntry.maximumLength()) return true;
                } else if (entry instanceof IntegerEntry integerEntry) {
                    try {
                        int value = Integer.parseInt(integerEntry.getUnsavedValue().toString());
                        if (value < integerEntry.minimum() || value > integerEntry.maximum()) return true;
                    } catch (Exception ignored) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isUpdatedConfig(String modID) {
        if (configEntries.containsKey(modID)) {
            for (CCEntry entry : configEntries.get(modID)) {
                if (entry.getValue() != entry.getUnsavedValue()) return true;
            }
        }
        return false;
    }

    public static boolean containsAnyInvalidConfigs() {
        for (String modID : configEntries.keySet()) if (isInvalidConfig(modID)) return true;
        return false;
    }

    public static boolean containsAnyUpdatedConfigs() {
        for (String modID : configEntries.keySet()) if (isUpdatedConfig(modID)) return true;
        return false;
    }

    /**
     * Will refresh all configurations back to their last saved state.
     */
    public static void refreshConfigs() {
        for (String modID : configEntries.keySet()) {
            for (CCEntry entry : configEntries.get(modID)) {
                entry.refreshValue();
            }
        }
    }

}
