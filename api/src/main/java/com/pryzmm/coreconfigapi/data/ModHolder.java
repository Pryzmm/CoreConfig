package com.pryzmm.coreconfigapi.data;

import org.jetbrains.annotations.ApiStatus;
import java.util.*;

@ApiStatus.Internal
public class ModHolder {

    private static final HashMap<String, ModData> configEntries = new HashMap<>();

    @ApiStatus.Internal
    public static void addRegisteredMod(String modID, String nameTranslation, String bannerPath, String overrideIconPath, Integer backgroundColor, Integer buttonColor) {
        ModData data = new ModData(modID, nameTranslation, bannerPath, overrideIconPath, backgroundColor, buttonColor);
        configEntries.put(modID, data);
    }
    @ApiStatus.Internal
    public static HashMap<String, ModData> getEntries() {
        return configEntries;
    }

}
