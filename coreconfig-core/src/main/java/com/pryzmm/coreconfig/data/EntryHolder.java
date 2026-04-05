package com.pryzmm.coreconfig.data;

import com.pryzmm.coreconfigapi.data.CCEntries;
import com.pryzmm.coreconfigapi.entry.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class EntryHolder {

    public static Collection<CCEntry> get(String modID) {
        List<CCEntry> entries = CCEntries.configEntries.getOrDefault(modID, new ArrayList<>());
        List<CCEntry> sortedEntries = new ArrayList<>();

        entries.stream()
            .filter(e -> e instanceof MainEntry mainEntry && mainEntry.divider() == null)
            .sorted((e1, e2) -> Integer.compare(e2.priority(), e1.priority()))
            .forEach(sortedEntries::add);

        List<DividerEntry> dividers = entries.stream()
            .filter(e -> e instanceof DividerEntry)
            .map(e -> (DividerEntry) e)
            .sorted((e1, e2) -> Integer.compare(e2.priority(), e1.priority()))
            .toList();

        if (dividers.isEmpty()) {
            return entries.stream()
                .sorted((e1, e2) -> Integer.compare(e2.priority(), e1.priority()))
                .toList();
        }

        for (DividerEntry divider : dividers) {
            sortedEntries.add(divider);
            entries.stream()
                .filter(e -> e instanceof MainEntry)
                .map(e -> (MainEntry) e)
                .filter(e -> e.divider() == divider)
                .sorted((e1, e2) -> Integer.compare(e2.priority(), e1.priority()))
                .forEach(sortedEntries::add);
        }

        return sortedEntries;
    }

    public static boolean isInvalidConfig(String modID) {
        if (CCEntries.configEntries.containsKey(modID)) {
            for (CCEntry entry : CCEntries.configEntries.get(modID)) {
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
            for (CCEntry e : CCEntries.configEntries.get(modID)) {
                if (e instanceof MainEntry entry) {
                         if (entry.getValue() instanceof Boolean && !Objects.equals(entry.getValue(),                                entry.getUnsavedValue()  )) return true;
                    else if (entry.getValue() instanceof String  && !Objects.equals(entry.getValue(),                                entry.getUnsavedValue()  )) return true;
                    else if (entry.getValue() instanceof Float   && !Objects.equals(entry.getValue(),   Float.valueOf(String.valueOf(entry.getUnsavedValue())))) return true;
                    else if (entry.getValue() instanceof Integer && !Objects.equals(entry.getValue(), Integer.valueOf(String.valueOf(entry.getUnsavedValue())))) return true;
                    else if (entry.getValue() instanceof Double  && !Objects.equals(entry.getValue(),  Double.valueOf(String.valueOf(entry.getUnsavedValue())))) return true;
                }
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
            for (CCEntry entry : CCEntries.configEntries.get(modID)) {
                if (entry instanceof MainEntry mainEntry) mainEntry.refreshValue();
            }
        }
    }

}
