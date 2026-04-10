package com.pryzmm.coreconfig.data;

import com.pryzmm.coreconfig.network.ServerPacketCommon;
import com.pryzmm.coreconfig.network.ServerSyncConfigPayload;
import com.pryzmm.coreconfig.services.Services;
import com.pryzmm.coreconfig.util.Server;
import com.pryzmm.coreconfigapi.Constants;
import com.pryzmm.coreconfigapi.data.CCFile;
import com.pryzmm.coreconfigapi.entry.CCEntry;
import com.pryzmm.coreconfigapi.entry.CustomEntry;
import com.pryzmm.coreconfigapi.entry.MainEntry;
import com.pryzmm.coreconfigapi.entry.WebsiteEntry;
import com.pryzmm.coreconfigapi.screen.IConfigScreen;
import net.minecraft.client.Minecraft;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CCFileHandler extends CCFile {

    private static File getConfigFile(String modName) {
        return new File("coreconfig/" + modName + ".yml");
    }
    private static File getServerConfigFile(String modName) {
        return new File("coreconfig/server_configs/" + modName + ".yml");
    }

    public static void updateConfigFile(String modName) {
        saveClientSideConfig(modName);
        if (Server.isHostingServer()) {
            ServerSyncConfigPayload payload = ServerPacketCommon.getSyncConfigPayload(modName);
            Services.NETWORK.sendToServer(payload);
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private static void saveClientSideConfig(String modName) {
        boolean requiresRestart = false;
        File configFile = getConfigFile(modName);
        if (!configFile.exists()) {
            Constants.LOGGER.info("Creating new config file for {}", modName);
            configFile.getParentFile().mkdirs();
            try { configFile.createNewFile(); }
            catch (Exception e) {
                Constants.LOGGER.error("Failed to create config file for {}: {}", modName, e.getMessage());
                return;
            }
        }
        try {
            boolean needsRestart = saveConfig(modName, configFile);
            if (needsRestart) requiresRestart = true;
        } catch (Exception e) { Constants.LOGGER.error("Failed to save config file for {}: {}", modName, e.getMessage()); }
        if (requiresRestart && Minecraft.getInstance().screen instanceof IConfigScreen screen) screen.sendRestartPopup();
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public static void saveServerSideConfig(String modName, Map<String, Object> values) {
        File configFile = getServerConfigFile(modName);
        if (!configFile.exists()) {
            Constants.LOGGER.info("Creating new server config directory for {}", modName);
            configFile.getParentFile().mkdirs();
            try { configFile.createNewFile(); }
            catch (Exception e) { Constants.LOGGER.error("Failed to create server config file for {}: {}", modName, e.getMessage()); }
        }
        try { saveConfig(modName, configFile, values); }
        catch (Exception e) { Constants.LOGGER.error("Failed to save server config file for {}: {}", modName, e.getMessage()); }
    }

    /**
     * @return Whether a restart is required to apply the changes in the config file
     * @throws Exception If an error occurs while saving the config file
     */
    private static boolean saveConfig(String modName, File configFile) throws Exception {
        return saveConfig(modName, configFile, null);
    }
    private static boolean saveConfig(String modName, File configFile, Map<String, Object> serverValues) throws Exception {
        boolean requiresRestart = false;
        List<String> existingLines;
        try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
            existingLines = reader.lines().toList();
        }
        List<String> newLines = new ArrayList<>();
        Collection<CCEntry> entries;
        if (serverValues == null) entries = EntryHolder.get(modName);
        else entries = EntryHolder.get(modName).stream()
                .filter(e -> e instanceof MainEntry)
                .map(e -> (MainEntry) e)
                .filter(e -> serverValues.containsKey(e.translation()))
                .map(e -> (CCEntry) e)
                .toList();
        for (CCEntry e : entries) {
            if (e instanceof MainEntry entry && !(e instanceof WebsiteEntry) && !(e instanceof CustomEntry)) {
                String key = entry.translation() + ":";
                String newLine = key + (serverValues != null ? serverValues.get(entry.translation()) : entry.getUnsavedValue());
                String oldLine = existingLines.stream()
                    .filter(l -> l.startsWith(key))
                    .findFirst()
                    .orElse(null);
                if (oldLine != null) {
                    String oldValue = oldLine.substring(oldLine.indexOf(":") + 1).trim();
                    String newValue = String.valueOf(serverValues != null ? serverValues.get(entry.translation()) : entry.getUnsavedValue());
                    if (!oldValue.equals(newValue) && entry.requiresRestart()) requiresRestart = true;
                } else if (entry.requiresRestart()) requiresRestart = true;
                entry.save();
                newLines.add(newLine);
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(configFile))) {
            writer.write(String.join("\n", newLines));
        }

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
                Constants.LOGGER.error("Unsupported config type: {}", clazz.getName());
                return null;
            }
            return clazz.cast(value);
        } catch (NumberFormatException e) {
            Constants.LOGGER.error("Failed to parse config value for {} as {}: {}", translation, clazz.getSimpleName(), e.getMessage());
        } catch (Exception e) {
            Constants.LOGGER.error("Failed to read config file for {}: {}", modName, e.getMessage());
        }
        return null;
    }

}
