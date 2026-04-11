package com.pryzmm.coreconfigapi.data;

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
        configEntries.computeIfAbsent(modID, k -> new ArrayList<>()).add(entry);
    }

}
