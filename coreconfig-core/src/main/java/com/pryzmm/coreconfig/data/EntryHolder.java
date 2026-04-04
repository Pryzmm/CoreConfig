package com.pryzmm.coreconfig.data;

import com.pryzmm.coreconfigapi.data.CCEntries;
import com.pryzmm.coreconfigapi.entry.FloatEntry;
import com.pryzmm.coreconfigapi.entry.IntegerEntry;
import com.pryzmm.coreconfigapi.entry.MainEntry;
import com.pryzmm.coreconfigapi.entry.StringEntry;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class EntryHolder {

    public static Collection<MainEntry> get(String modID) {
        return CCEntries.configEntries.getOrDefault(modID, new ArrayList<>()).stream().sorted((e1, e2) -> Integer.compare(e2.priority(), e1.priority())).toList();
    }

    public static boolean isInvalidConfig(String modID) {
        if (CCEntries.configEntries.containsKey(modID)) {
            for (MainEntry entry : CCEntries.configEntries.get(modID)) {
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
                } else if (entry instanceof FloatEntry floatEntry) {
                    try {
                        float value = Float.parseFloat(floatEntry.getUnsavedValue().toString());
                        if (value < floatEntry.minimum() || value > floatEntry.maximum()) return true;
                    } catch (Exception ignored) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isUpdatedConfig(String modID) {
        if (CCEntries.configEntries.containsKey(modID)) {
            for (MainEntry entry : CCEntries.configEntries.get(modID)) {
                     if (entry.getValue() instanceof Boolean && !Objects.equals(entry.getValue(),                                entry.getUnsavedValue()  )) return true;
                else if (entry.getValue() instanceof String  && !Objects.equals(entry.getValue(),                                entry.getUnsavedValue()  )) return true;
                else if (entry.getValue() instanceof Float   && !Objects.equals(entry.getValue(),   Float.valueOf(String.valueOf(entry.getUnsavedValue())))) return true;
                else if (entry.getValue() instanceof Integer && !Objects.equals(entry.getValue(), Integer.valueOf(String.valueOf(entry.getUnsavedValue())))) return true;
                else if (entry.getValue() instanceof Double  && !Objects.equals(entry.getValue(),  Double.valueOf(String.valueOf(entry.getUnsavedValue())))) return true;
            }
        }
        return false;
    }

    public static boolean containsAnyInvalidConfigs() {
        for (String modID : CCEntries.configEntries.keySet()) if (isInvalidConfig(modID)) return true;
        return false;
    }

    public static boolean containsAnyUpdatedConfigs() {
        for (String modID : CCEntries.configEntries.keySet()) if (isUpdatedConfig(modID)) return true;
        return false;
    }

    /**
     * Will refresh all configurations back to their last saved state.
     */
    public static void refreshConfigs() {
        for (String modID : CCEntries.configEntries.keySet()) {
            for (MainEntry entry : CCEntries.configEntries.get(modID)) {
                entry.refreshValue();
            }
        }
    }

}
