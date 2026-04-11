package com.pryzmm.coreconfigapi.entry;

import com.pryzmm.coreconfigapi.Constants;
import com.pryzmm.coreconfigapi.component.ImageComponent;
import com.pryzmm.coreconfigapi.data.CCEntries;
import com.pryzmm.coreconfigapi.data.CCFile;
import com.pryzmm.coreconfigapi.data.ConfigType;
import com.pryzmm.coreconfigapi.data.ConfigValidity;
import net.minecraft.network.chat.Component;

public class BooleanEntry implements MainEntry {

    private boolean value;
    private boolean defaultValue;
    private Boolean newValue = null;
    private Component descriptor;
    private ImageComponent image;
    private String translation;
    private String modID;
    private Integer hoverColor;
    private boolean requiresRestart;
    private int priority;
    private DividerEntry divider;
    private ConfigType type;

    private BooleanEntry() {}

    public Component descriptor() { return descriptor; }
    public boolean requiresRestart() { return requiresRestart; }
    public String translation() { return translation; }
    public String modID() { return modID; }
    public Integer hoverColor() { return hoverColor; }
    public int priority() { return priority; }
    public ImageComponent image() { return image; }
    public DividerEntry divider() { return divider; }
    public ConfigType type() { return type; }

    public Boolean getUnsavedValue() { return newValue != null ? newValue : value; }
    public Boolean getDefaultValue() { return defaultValue; }
    public Boolean getValue() {
        return getServerValue() != null ? getServerValue() : value;
    }
    public Boolean getClientValue() { return value; }
    public Boolean getServerValue() {
        return CCFile.getInstance().getServerValue(modID, translation, Boolean.class);
    }
    public void setValue(boolean value) { this.value = value; }

    public void refreshValue() { this.newValue = value; }

    public void change(boolean newValue) { this.newValue = newValue; }

    public void save() {
        if (newValue != null) {
            if (!requiresRestart) setValue(newValue);
        }
    }

    public static class Builder {
        private final boolean defaultValue;
        private final String translation;
        private final String modID;
        private Component descriptor = Component.empty();
        private ImageComponent image = null;
        private boolean requiresRestart = false;
        private Integer hoverColor = null;
        private int priority = 0;
        private DividerEntry divider = null;
        private ConfigType type = ConfigType.CLIENT;

        public Builder(String modID, String translation, boolean defaultValue) {
            this.defaultValue = defaultValue;
            this.translation = translation;
            this.modID = modID;
        }

        public Builder descriptor(Component descriptor) { this.descriptor = descriptor; return this; }
        public Builder requiresRestart(boolean requiresRestart) { this.requiresRestart = requiresRestart; return this; }
        public Builder hoverColor(int hoverColor) { this.hoverColor = hoverColor; return this; }
        public Builder priority(int priority) { this.priority = priority; return this; }
        public Builder divider(DividerEntry divider) { this.divider = divider; return this; }
        public Builder image(String path, int width, int height) { this.image = new ImageComponent(this.modID, path, width, height); return this; }
        public Builder image(String path, int width, int height, int frameHeight, int ticks) { this.image = new ImageComponent(this.modID, path, width, height, frameHeight, ticks); return this; }
        public Builder type(ConfigType type) { this.type = type; return this; }

        public BooleanEntry build() {
            BooleanEntry entry = new BooleanEntry();
            entry.value = defaultValue;
            entry.modID = modID;
            entry.translation = translation;
            entry.descriptor = descriptor;
            entry.image = image;
            entry.requiresRestart = requiresRestart;
            entry.hoverColor = hoverColor;
            entry.priority = priority;
            entry.defaultValue = defaultValue;
            entry.divider = divider;
            entry.type = type;

            Boolean configValue = CCFile.getInstance().getConfigValue(modID, translation, Boolean.class);

            boolean validated = ConfigValidity.validateBooleanConfig(configValue != null ? configValue.toString() : String.valueOf(defaultValue));

            if (configValue != null && validated) entry.value = configValue;
            else if (!validated) Constants.LOGGER.error("Failed to validate config value for {}:{}. Using default value: {}", modID, translation, defaultValue);

            CCEntries.addEntry(entry.modID(), entry);
            return entry;
        }
    }

}
