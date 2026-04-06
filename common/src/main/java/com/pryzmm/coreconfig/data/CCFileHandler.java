package com.pryzmm.coreconfig.data;

import com.pryzmm.coreconfigapi.Constants;
import com.pryzmm.coreconfigapi.data.CCFile;
import com.pryzmm.coreconfigapi.entry.CCEntry;
import com.pryzmm.coreconfigapi.entry.MainEntry;
import com.pryzmm.coreconfigapi.entry.WebsiteEntry;
import com.pryzmm.coreconfigapi.screen.ConfigScreen;
import net.minecraft.client.Minecraft;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CCFileHandler extends CCFile {

    private static File getConfigFile(String modName) {
        return new File("coreconfig/" + modName + ".yml");
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void updateConfigFile(String modName) {
        boolean requiresRestart = false;
        File configFile = getConfigFile(modName);
        if (!configFile.exists()) {
            Constants.LOGGER.info("Creating new config file for " + modName);
            configFile.getParentFile().mkdirs();
            try { configFile.createNewFile(); } catch (Exception e) {
                Constants.LOGGER.severe("Failed to create config file for " + modName + ": " + e.getMessage());
                return;
            }
        }
        try {
            boolean needsRestart = saveConfig(modName, configFile);
            if (needsRestart) requiresRestart = true;
        } catch (Exception e) {
            Constants.LOGGER.severe("Failed to save config file for " + modName + ": " + e.getMessage());
        }
        if (requiresRestart && Minecraft.getInstance().screen instanceof ConfigScreen screen) screen.sendRestartPopup();
    }

    /**
     * @return Whether a restart is required to apply the changes in the config file
     * @throws Exception If an error occurs while saving the config file
     */
    private static boolean saveConfig(String modName, File configFile) throws Exception {
        boolean requiresRestart = false;
        List<String> existingLines;
        try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
            existingLines = reader.lines().toList();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(configFile));
        List<String> newLines = new ArrayList<>();
        for (CCEntry e : EntryHolder.get(modName)) {
            if (e instanceof MainEntry entry && !(e instanceof WebsiteEntry)) {
                String key = entry.translation() + ":";
                String newLine = key + entry.getUnsavedValue();
                String oldLine = existingLines.stream()
                    .filter(l -> l.startsWith(key))
                    .findFirst()
                    .orElse(null);
                if (oldLine != null) {
                    String oldValue = oldLine.substring(oldLine.indexOf(":") + 1).trim();
                    String newValue = String.valueOf(entry.getUnsavedValue());
                    if (!oldValue.equals(newValue) && entry.requiresRestart()) requiresRestart = true;
                } else if (entry.requiresRestart()) requiresRestart = true;
                entry.save();
                newLines.add(newLine);
            }
        }

        writer.write(String.join("\n", newLines));
        writer.close();

        return requiresRestart;
    }

    @Override
    public <T> T getConfigValue(String modName, String translation, Class<T> clazz) {
        File configFile = getConfigFile(modName);
        if (!configFile.exists()) return null;
        try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
            String line = reader.lines()
                .filter(l -> l.startsWith(translation + ":"))
                .findFirst()
                .orElse(null);
            if (line == null) return null;
            String valueStr = line.substring(line.indexOf(":") + 1).trim();
            Object value;
            if (clazz == Boolean.class)      value = Boolean.parseBoolean(valueStr);
            else if (clazz == String.class)  value = valueStr;
            else if (clazz == Integer.class) value = Integer.parseInt(valueStr);
            else if (clazz == Float.class)   value = Float.parseFloat(valueStr);
            else if (clazz == Double.class)  value = Double.parseDouble(valueStr);
            else {
                Constants.LOGGER.severe("Unsupported config type: " + clazz.getName());
                return null;
            }
            return clazz.cast(value);
        } catch (NumberFormatException e) {
            Constants.LOGGER.severe("Failed to parse config value for " + translation + " as " + clazz.getSimpleName() + ": " + e.getMessage());
        } catch (Exception e) {
            Constants.LOGGER.severe("Failed to read config file for " + modName + ": " + e.getMessage());
        }
        return null;
    }

}
