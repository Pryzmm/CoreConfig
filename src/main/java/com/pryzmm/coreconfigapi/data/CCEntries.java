package com.pryzmm.coreconfigapi.data;

import com.pryzmm.coreconfigapi.Constants;
import com.pryzmm.coreconfigapi.entry.CCEntry;
import org.jetbrains.annotations.ApiStatus;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Stores configuration entries
 */
@ApiStatus.Internal
public class CCEntries {

    /**
     * A HashMap of configuration entries stored by modID
     */
    @ApiStatus.Internal
    public static final HashMap<String, ArrayList<CCEntry>> configEntries = new HashMap<>();

    /**
     * adds an entry to a mod's config list
     * @param modID The mod ID
     * @param entry the entry to add
     */
    @ApiStatus.Internal
    public static void addEntry(String modID, CCEntry entry) {
        boolean duplicateEntry = configEntries.get(modID) != null && !configEntries.get(modID).stream()
            .filter(e -> e.translation().equals(entry.translation()))
            .toList().isEmpty();
        if (duplicateEntry) {
            Constants.LOGGER.error("Found a duplicate configuration translation string. Only one translation string of this value may be registered! String: '{}'", entry.translation());
            return;
        }
        configEntries.computeIfAbsent(modID, k -> new ArrayList<>()).add(entry);
    }

}
