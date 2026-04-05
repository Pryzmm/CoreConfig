package com.pryzmm.coreconfigapi.data;

import com.pryzmm.coreconfigapi.entry.CCEntry;
import java.util.ArrayList;
import java.util.HashMap;

public class CCEntries {

    public static final HashMap<String, ArrayList<CCEntry>> configEntries = new HashMap<>();

    public static void addEntry(String modID, CCEntry entry) {
        configEntries.computeIfAbsent(modID, k -> new ArrayList<>()).add(entry);
    }

}
