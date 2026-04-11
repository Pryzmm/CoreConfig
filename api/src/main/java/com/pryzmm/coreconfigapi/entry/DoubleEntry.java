package com.pryzmm.coreconfigapi.entry;

import com.pryzmm.coreconfigapi.Constants;
import com.pryzmm.coreconfigapi.component.ImageComponent;
import com.pryzmm.coreconfigapi.data.CCEntries;
import com.pryzmm.coreconfigapi.data.CCFile;
import com.pryzmm.coreconfigapi.data.ConfigType;
import com.pryzmm.coreconfigapi.data.ConfigValidity;

/**
 * A configuration option for CoreConfig, dedicated towards doubles. Use the {@link Builder} to create a new DoubleEntry for use in mods.
 * <p>
 * Visit <a href="https://github.com/Pryzmm/CoreConfig/wiki/Entries#doubleentry">the wiki page</a> to view more detailed documentation regarding this entry and its builder methods.
 */
public class DoubleEntry implements MainEntry {

    private Double value;
    private Double defaultValue;
    private String newValue = null;
    private Object descriptor;
    private ImageComponent image;
    private String modID;
    private String translation;
    private Integer hoverColor;
    private boolean requiresRestart;
    private double minimum;
    private double maximum;
    private int priority;
    private DividerEntry divider;
    private ConfigType type;

    private DoubleEntry() {}

    public Object descriptor() { return descriptor; }
    public ImageComponent image() { return image; }
    public boolean requiresRestart() { return requiresRestart; }
    public String modID() { return modID; }
    public String translation() { return translation; }
    public Integer hoverColor() { return hoverColor; }
    public int priority() { return priority; }
    public Double minimum() { return minimum; }
    public Double maximum() { return maximum; }
    public DividerEntry divider() { return divider; }
    public ConfigType type() { return type; }

    public Object getUnsavedValue() { return newValue != null ? newValue : value; }
    public Double getDefaultValue() { return defaultValue; }
    public Double getValue() {
        return getServerValue() != null ? getServerValue() : value;
    }
    public Double getClientValue() { return value; }
    public Double getServerValue() {
        return CCFile.getInstance().getServerValue(modID, translation, Double.class);
    }
    public void setValue(Double value) { this.value = value; }

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
            if (!requiresRestart) setValue(Double.valueOf(newValue));
        }
    }

    public static class Builder {
        private final Double defaultValue;
        private final String modID;
        private final String translation;
        private Object descriptor;
        private ImageComponent image = null;
        private boolean requiresRestart = false;
        private Integer hoverColor = null;
        private double minimum = -Double.MAX_VALUE;
        private double maximum = Double.MAX_VALUE;
        private int priority = 0;
        private DividerEntry divider = null;
        private ConfigType type = ConfigType.CLIENT;

        public Builder(String modID, String translation, Double defaultValue) {
            this.defaultValue = defaultValue == null ? 0 : defaultValue;
            this.translation = translation;
            this.modID = modID;
        }

        public Builder descriptor(Object descriptor) { this.descriptor = descriptor; return this; }
        public Builder requiresRestart(boolean requiresRestart) { this.requiresRestart = requiresRestart; return this; }
        public Builder hoverColor(int hoverColor) { this.hoverColor = hoverColor; return this; }
        public Builder minimum(float minimum) { this.minimum = minimum; return this; }
        public Builder maximum(float maximum) { this.maximum = maximum; return this; }
        public Builder priority(int priority) { this.priority = priority; return this; }
        public Builder divider(DividerEntry divider) { this.divider = divider; return this; }
        public Builder image(String path, int width, int height) { this.image = new ImageComponent(this.modID, path, width, height); return this; }
        public Builder image(String path, int width, int height, int frameHeight, int ticks) { this.image = new ImageComponent(this.modID, path, width, height, frameHeight, ticks); return this; }
        public Builder type(ConfigType type) { this.type = type; return this; }

        public DoubleEntry build() {
            DoubleEntry entry = new DoubleEntry();
            entry.value = defaultValue;
            entry.translation = translation;
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

            Double configValue = CCFile.getInstance().getConfigValue(modID, translation, Double.class);

            if (!ConfigValidity.validateDoubleConfig(String.valueOf(defaultValue), entry.minimum, entry.maximum)) {
                Constants.LOGGER.warn("Default value for {}:{} is out of bounds. This may have unintended consequences.", modID, translation);
            }

            boolean validated = ConfigValidity.validateDoubleConfig(configValue != null ? String.valueOf(configValue) : String.valueOf(defaultValue), entry.minimum, entry.maximum);

            if (configValue != null && validated) entry.value = configValue;
            else if (!validated) Constants.LOGGER.error("Failed to validate config value for {}:{}. Using default value: {}", modID, translation, defaultValue);

            CCEntries.addEntry(entry.modID(), entry);
            return entry;
        }
    }
}
