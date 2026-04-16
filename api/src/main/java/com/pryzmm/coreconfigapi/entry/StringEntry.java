package com.pryzmm.coreconfigapi.entry;

import com.pryzmm.coreconfigapi.component.ImageComponent;
import com.pryzmm.coreconfigapi.data.CCEntries;
import com.pryzmm.coreconfigapi.data.CCFile;
import com.pryzmm.coreconfigapi.data.ConfigType;

/**
 * A configuration option for CoreConfig, dedicated towards strings. Use the {@link Builder} to create a new StringEntry for use in mods.
 * <p>
 * Visit <a href="https://github.com/Pryzmm/CoreConfig/wiki/Entries#stringentry">the wiki page</a> to view more detailed documentation regarding this entry and its builder methods.
 */
public class StringEntry implements MainEntry {

    private String value;
    private String defaultValue;
    private String newValue = null;
    private Object descriptor;
    private ImageComponent image;
    private String translation;
    private String modID;
    private Integer hoverColor;
    private boolean requiresRestart;
    private int minimumLength;
    private int maximumLength;
    private int priority;
    private DividerEntry divider;
    private ConfigType type;

    private StringEntry() {}

    public Object descriptor() { return descriptor; }
    public ImageComponent image() { return image; }
    public boolean requiresRestart() { return requiresRestart; }
    public String translation() { return translation; }
    public String modID() { return modID; }
    public Integer hoverColor() { return hoverColor; }
    public int priority() { return priority; }
    public Integer minimumLength() { return minimumLength; }
    public Integer maximumLength() { return maximumLength; }
    public DividerEntry divider() { return divider; }
    public ConfigType type() { return type; }

    public String getUnsavedValue() { return newValue != null ? newValue : value; }
    public String getDefaultValue() { return defaultValue; }
    public String getValue() {
        return getServerValue() != null ? getServerValue() : value;
    }
    public String getClientValue() {
        return value;
    }
    public String getServerValue() {
        return CCFile.getInstance().getServerValue(modID, translation, String.class);
    }
    public void setValue(String value) { this.value = value; }

    public void refreshValue() { this.newValue = value; }

    public void change(String newValue) { this.newValue = newValue; }

    public void save() {
        if (newValue != null) {
            if (!requiresRestart) setValue(newValue);
        }
    }

    public static class Builder {
        private final String defaultValue;
        private final String translation;
        private final String modID;
        private Object descriptor;
        private ImageComponent image = null;
        private boolean requiresRestart = false;
        private Integer hoverColor = null;
        private int minimumLength = 0;
        private int maximumLength = Integer.MAX_VALUE;
        private int priority = 0;
        private DividerEntry divider = null;
        private ConfigType type = ConfigType.CLIENT;

        public Builder(String modID, String translation, String defaultValue) {
            this.defaultValue = defaultValue == null ? "" : defaultValue;
            this.modID = modID;
            this.translation = translation;
        }

        public Builder descriptor(Object descriptor) { this.descriptor = descriptor; return this; }
        public Builder requiresRestart(boolean requiresRestart) { this.requiresRestart = requiresRestart; return this; }
        public Builder hoverColor(int hoverColor) { this.hoverColor = hoverColor; return this; }
        public Builder minimumLength(int minimumLength) { this.minimumLength = minimumLength; return this; }
        public Builder maximumLength(int maximumLength) { this.maximumLength = maximumLength; return this; }
        public Builder priority(int priority) { this.priority = priority; return this; }
        public Builder divider(DividerEntry divider) { this.divider = divider; return this; }
        public Builder image(String path, int width, int height) { this.image = new ImageComponent(this.modID, path, width, height); return this; }
        public Builder image(String path, int width, int height, int frameHeight, int ticks) { this.image = new ImageComponent(this.modID, path, width, height, frameHeight, ticks); return this; }
        public Builder type(ConfigType type) { this.type = type; return this; }

        public StringEntry build() {
            StringEntry entry = new StringEntry();
            entry.modID = modID;
            entry.value = defaultValue;
            entry.translation = translation;
            entry.modID = modID;
            entry.descriptor = descriptor;
            entry.image = image;
            entry.requiresRestart = requiresRestart;
            entry.hoverColor = hoverColor;
            entry.minimumLength = Math.max(minimumLength, 1);
            entry.maximumLength = (maximumLength == Integer.MAX_VALUE) ? Integer.MAX_VALUE - 1 : Math.max(maximumLength, 1);
            entry.priority = priority;
            entry.defaultValue = defaultValue;
            entry.divider = divider;
            entry.type = type;

            String configValue = CCFile.getInstance().getConfigValue(modID, translation, String.class);
            if (configValue != null) entry.value = configValue;

            CCEntries.addEntry(modID, entry);
            return entry;
        }
    }
}
