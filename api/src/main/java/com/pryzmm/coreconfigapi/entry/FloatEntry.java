package com.pryzmm.coreconfigapi.entry;

import com.pryzmm.coreconfigapi.Constants;
import com.pryzmm.coreconfigapi.component.ImageComponent;
import com.pryzmm.coreconfigapi.data.CCEntries;
import com.pryzmm.coreconfigapi.data.CCFile;
import com.pryzmm.coreconfigapi.data.ConfigType;
import com.pryzmm.coreconfigapi.data.ConfigValidity;
import net.minecraft.network.chat.Component;

public class FloatEntry implements MainEntry {

    private Float value;
    private Float defaultValue;
    private String newValue = null;
    private Component descriptor;
    private ImageComponent image;
    private String translation;
    private String modID;
    private Integer hoverColor;
    private boolean requiresRestart;
    private float minimum;
    private float maximum;
    private int priority;
    private DividerEntry divider;
    private ConfigType type;

    private FloatEntry() {}

    public Component descriptor() { return descriptor; }
    public ImageComponent image() { return image; }
    public boolean requiresRestart() { return requiresRestart; }
    public String translation() { return translation; }
    public String modID() { return modID; }
    public Integer hoverColor() { return hoverColor; }
    public int priority() { return priority; }
    public Float minimum() { return minimum; }
    public Float maximum() { return maximum; }
    public DividerEntry divider() { return divider; }
    public ConfigType type() { return type; }

    public Object getUnsavedValue() { return newValue != null ? newValue : value; }
    public Float getDefaultValue() { return defaultValue; }
    public Float getValue() {
        return getServerValue() != null ? getServerValue() : value;
    }
    public Float getClientValue() { return value; }
    public Float getServerValue() {
        return CCFile.getInstance().getServerValue(modID, translation, Float.class);
    }
    public void setValue(Float value) { this.value = value; }

    public void refreshValue() { this.newValue = String.valueOf(value); }

    public boolean change(Object newValue) {
        boolean changed = false;
        try {
            this.newValue = (String) newValue;
            changed = true;
        } catch (Exception ignored) {}
        return changed;
    }

    public void save() {
        if (newValue != null) {
            if (!requiresRestart) setValue(Float.valueOf(newValue));
        }
    }

    public static class Builder {
        private final Float defaultValue;
        private final String translation;
        private final String modID;
        private Component descriptor = Component.empty();
        private ImageComponent image = null;
        private boolean requiresRestart = false;
        private Integer hoverColor = null;
        private float minimum = Float.MIN_VALUE;
        private float maximum = Float.MAX_VALUE;
        private int priority = 0;
        private DividerEntry divider = null;
        private ConfigType type = ConfigType.CLIENT;

        public Builder(String modID, String translation, Float defaultValue) {
            this.defaultValue = defaultValue == null ? 0 : defaultValue;
            this.translation = translation;
            this.modID = modID;
        }

        public Builder descriptor(Component descriptor) { this.descriptor = descriptor; return this; }
        public Builder requiresRestart(boolean requiresRestart) { this.requiresRestart = requiresRestart; return this; }
        public Builder hoverColor(int hoverColor) { this.hoverColor = hoverColor; return this; }
        public Builder minimum(float minimum) { this.minimum = minimum; return this; }
        public Builder maximum(float maximum) { this.maximum = maximum; return this; }
        public Builder priority(int priority) { this.priority = priority; return this; }
        public Builder divider(DividerEntry divider) { this.divider = divider; return this; }
        public Builder image(String path, int width, int height) { this.image = new ImageComponent(this.modID, path, width, height); return this; }
        public Builder image(String path, int width, int height, int frameHeight, int ticks) { this.image = new ImageComponent(this.modID, path, width, height, frameHeight, ticks); return this; }
        public Builder type(ConfigType type) { this.type = type; return this; }

        public FloatEntry build() {
            FloatEntry entry = new FloatEntry();
            entry.value = defaultValue;
            entry.translation = translation;
            entry.modID = modID;
            entry.descriptor = descriptor;
            entry.requiresRestart = requiresRestart;
            entry.hoverColor = hoverColor;
            entry.minimum = minimum;
            entry.maximum = maximum;
            entry.priority = priority;
            entry.defaultValue = defaultValue;
            entry.image = image;
            entry.divider = divider;
            entry.type = type;

            Float configValue = CCFile.getInstance().getConfigValue(modID, translation, Float.class);

            if (!ConfigValidity.validateFloatConfig(String.valueOf(defaultValue), entry.minimum, entry.maximum)) {
                Constants.LOGGER.warn("Default value for {}:{} is out of bounds. This may have unintended consequences.", modID, translation);
            }

            boolean validated = ConfigValidity.validateFloatConfig(configValue != null ? String.valueOf(configValue) : String.valueOf(defaultValue), entry.minimum, entry.maximum);

            if (configValue != null && validated) entry.value = configValue;
            else if (!validated) Constants.LOGGER.error("Failed to validate config value for {}:{}. Using default value: {}", modID, translation, defaultValue);

            CCEntries.addEntry(modID, entry);
            return entry;
        }
    }
}
