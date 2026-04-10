package com.pryzmm.coreconfigapi.entry;

import com.pryzmm.coreconfigapi.Constants;
import com.pryzmm.coreconfigapi.component.ImageComponent;
import com.pryzmm.coreconfigapi.data.CCEntries;
import com.pryzmm.coreconfigapi.data.CCFile;
import com.pryzmm.coreconfigapi.data.ConfigType;
import com.pryzmm.coreconfigapi.data.ConfigValidity;
import net.minecraft.network.chat.Component;

public class ColorEntry implements MainEntry {

    private Integer value;
    private Integer defaultValue;
    private Integer newValue = null;
    private Component descriptor;
    private ImageComponent image;
    private String modID;
    private String translation;
    private Integer hoverColor;
    private boolean requiresRestart;
    private int priority;
    private boolean allowAlpha;
    private DividerEntry divider;
    private ConfigType type;

    private ColorEntry() {}

    public Component descriptor() { return descriptor; }
    public ImageComponent image() { return image; }
    public boolean requiresRestart() { return requiresRestart; }
    public String modID() { return modID; }
    public String translation() { return translation; }
    public Integer hoverColor() { return hoverColor; }
    public int priority() { return priority; }
    public DividerEntry divider() { return divider; }
    public ConfigType type() { return type; }
    public boolean allowAlpha() { return allowAlpha; }

    public Object getUnsavedValue() { return newValue != null ? newValue : value; }
    public Integer getDefaultValue() { return defaultValue; }
    public Integer getValue() { return value; }
    public void setValue(Integer value) { this.value = value; }

    public void refreshValue() { this.newValue = value; }

    public boolean change(Object newValue) {
        boolean changed = false;
        try {
            this.newValue = (Integer) newValue;
            changed = true;
        } catch (Exception ignored) {}
        return changed;
    }

    public void save() {
        if (newValue != null) {
            if (!requiresRestart) setValue(newValue);
        }
    }

    public static class Builder {
        private final String modID;
        private final String translation;
        private final Integer defaultValue;
        private Component descriptor = Component.empty();
        private ImageComponent image = null;
        private boolean requiresRestart = false;
        private Integer hoverColor = null;
        private int priority = 0;
        private DividerEntry divider = null;
        private boolean allowAlpha = false;
        private ConfigType type = ConfigType.CLIENT;

        public Builder(String modID, String translation, Integer defaultValue) {
            this.defaultValue = defaultValue == null ? 0 : defaultValue;
            this.translation = translation;
            this.modID = modID;
        }

        public Builder descriptor(Component descriptor) { this.descriptor = descriptor; return this; }
        public Builder requiresRestart(boolean requiresRestart) { this.requiresRestart = requiresRestart; return this; }
        public Builder hoverColor(int hoverColor) { this.hoverColor = hoverColor; return this; }
        public Builder priority(int priority) { this.priority = priority; return this; }
        public Builder divider(DividerEntry divider) { this.divider = divider; return this; }
        public Builder allowAlpha(boolean allowAlpha) { this.allowAlpha = allowAlpha; return this; }
        public Builder image(String path, int width, int height) { this.image = new ImageComponent(this.modID, path, width, height); return this; }
        public Builder image(String path, int width, int height, int frameHeight, int ticks) { this.image = new ImageComponent(this.modID, path, width, height, frameHeight, ticks); return this; }
        public Builder type(ConfigType type) { this.type = type; return this; }

        public ColorEntry build() {
            ColorEntry entry = new ColorEntry();
            entry.value = defaultValue;
            entry.translation = translation;
            entry.modID = modID;
            entry.descriptor = descriptor;
            entry.requiresRestart = requiresRestart;
            entry.hoverColor = hoverColor;
            entry.priority = priority;
            entry.defaultValue = defaultValue;
            entry.image = image;
            entry.divider = divider;
            entry.allowAlpha = allowAlpha;
            entry.type = type;

            Integer configValue = CCFile.getInstance().getConfigValue(modID, translation, Integer.class);

            boolean validated = ConfigValidity.validateColorConfig(configValue != null ? configValue.toString() : String.valueOf(defaultValue));

            if (configValue != null && validated) entry.value = configValue;
            else if (!validated) Constants.LOGGER.error("Failed to validate config value for {}:{}. Using default value: {}", modID, translation, defaultValue);

            CCEntries.addEntry(entry.modID(), entry);
            return entry;
        }
    }
}
