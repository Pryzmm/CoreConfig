package com.pryzmm.coreconfig.data;

import com.pryzmm.coreconfig.network.ServerPacketCommon;
import com.pryzmm.coreconfig.network.ServerSyncConfigPayload;
import com.pryzmm.coreconfig.services.Services;
import com.pryzmm.coreconfig.network.Server;
import com.pryzmm.coreconfigapi.Constants;
import com.pryzmm.coreconfigapi.data.CCFile;
import com.pryzmm.coreconfigapi.entry.CCEntry;
import com.pryzmm.coreconfigapi.entry.CustomEntry;
import com.pryzmm.coreconfigapi.entry.MainEntry;
import com.pryzmm.coreconfigapi.entry.WebsiteEntry;
import com.pryzmm.coreconfigapi.registrar.ConfigRegistrar;
import com.pryzmm.coreconfigapi.screen.IConfigScreen;
import net.minecraft.client.Minecraft;
import java.io.*;
import java.util.*;

public class CCFileHandler extends CCFile implements ConfigRegistrar {

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
        Map<String, Object> configMap = CCFileHandler.serverConfigs.get(modName);
        if (configMap == null) configMap = new HashMap<>();
        configMap.putAll(values);
        CCFileHandler.serverConfigs.put(modName, configMap);
    }

    private static boolean saveConfig(String modName, File configFile) throws Exception {
        boolean requiresRestart = false;
        List<String> existingLines;
        try (BufferedReader reader = new BufferedReader(new FileReader(configFile))) {
            existingLines = reader.lines().toList();
        }
        List<String> newLines = new ArrayList<>();
        Collection<CCEntry> entries = EntryHolder.get(modName);
        for (CCEntry e : entries) {
            if (e instanceof MainEntry entry && !(e instanceof WebsiteEntry) && !(e instanceof CustomEntry)) {
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

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(configFile))) {
            writer.write(String.join("\n", newLines));
        }

        return requiresRestart;
    }

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
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
            if      (clazz == Boolean.class) value = Boolean.parseBoolean(valueStr);
            else if (clazz ==  String.class) value = valueStr;
            else if (clazz == Integer.class) value = Integer.parseInt(valueStr);
            else if (clazz ==   Float.class) value = Float.parseFloat(valueStr);
            else if (clazz ==  Double.class) value = Double.parseDouble(valueStr);
            else if (clazz ==    Long.class) value = Long.parseLong(valueStr);
            else if (clazz.        isEnum()) value = Enum.valueOf((Class<Enum>) clazz, valueStr);
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

    public static Map<String, Map<String, Object>> serverConfigs = new HashMap<>();

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public <T> T getServerValue(String modName, String translation, Class<T> clazz) {
        Map<String, Object> config = serverConfigs.get(modName);
        if (config == null) return null;
        Object raw = config.get(translation);
        if (raw == null) return null;
        if (clazz.isEnum() && raw instanceof String valueStr)
            return clazz.cast(Enum.valueOf((Class<Enum>) clazz, valueStr));
        return (T) raw;
    }
}
