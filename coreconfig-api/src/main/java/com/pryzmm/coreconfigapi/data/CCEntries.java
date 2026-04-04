package com.pryzmm.coreconfigapi.data;

import com.pryzmm.coreconfigapi.entry.MainEntry;
import java.util.ArrayList;
import java.util.HashMap;

public class CCEntries {

    public static final HashMap<String, ArrayList<MainEntry>> configEntries = new HashMap<>();

    public static void addEntry(String modID, MainEntry entry) {
        configEntries.computeIfAbsent(modID, k -> new ArrayList<>()).add(entry);
    }

}
