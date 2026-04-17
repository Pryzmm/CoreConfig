package com.pryzmm.coreconfigapi.entry;

import com.pryzmm.coreconfigapi.Constants;
import com.pryzmm.coreconfigapi.component.ImageComponent;
import com.pryzmm.coreconfigapi.data.CCEntries;
import com.pryzmm.coreconfigapi.data.CCFile;
import com.pryzmm.coreconfigapi.data.ConfigType;
import com.pryzmm.coreconfigapi.data.ConfigValidity;

/**
 * A configuration option for CoreConfig, dedicated towards enums. Use the {@link Builder} to create a new EnumEntry for use in mods.
 * <p>
 * Visit <a href="https://github.com/Pryzmm/CoreConfig/wiki/Entries#enumentry">the wiki page</a> to view more detailed documentation regarding this entry and its builder methods.
 */
public class EnumEntry implements MainEntry {

    private String value;
    private String defaultValue;
    private String newValue = null;
    private Class<?> enumClass;
    private Object descriptor;
    private ImageComponent image;
    private String translation;
    private String modID;
    private Integer hoverColor;
    private boolean requiresRestart;
    private int priority;
    private DividerEntry divider;
    private ConfigType type;

    private EnumEntry() {}

    public Object descriptor() { return descriptor; }
    public boolean requiresRestart() { return requiresRestart; }
    public String translation() { return translation; }
    public String modID() { return modID; }
    public Integer hoverColor() { return hoverColor; }
    public int priority() { return priority; }
    public ImageComponent image() { return image; }
    public DividerEntry divider() { return divider; }
    public ConfigType type() { return type; }
    public Class<?> enumClass() { return enumClass; }


    public Enum<?> getUnsavedValue() { return resolveEnum(newValue != null ? newValue : value); }
    public Enum<?> getDefaultValue() { return resolveEnum(defaultValue); }
    public Enum<?> getValue() { return getServerValue() != null ? getServerValue() : resolveEnum(value); }
    public Enum<?> getClientValue() { return resolveEnum(value); }
    public Enum<?> getServerValue() { return (Enum<?>) CCFile.getInstance().getServerValue(modID, translation, enumClass); }

    @SuppressWarnings("unchecked")
    public <T extends Enum<T>> T resolveEnum(String name) {
        return Enum.valueOf((Class<T>) enumClass, name);
    }

    public void setValue(Enum<?> value) { this.value = value.name(); }

    public void refreshValue() { this.newValue = value; }

    public void change(Enum<?> newValue) { this.newValue = newValue.name(); }

    public void save() {
        if (newValue != null) {
            if (!requiresRestart) setValue(resolveEnum(newValue));
        }
    }

    public static class Builder {
        private final Enum<?> defaultValue;
        private final String translation;
        private final String modID;
        private final Class<?> enumClass;
        private Object descriptor;
        private ImageComponent image = null;
        private boolean requiresRestart = false;
        private Integer hoverColor = null;
        private int priority = 0;
        private DividerEntry divider = null;
        private ConfigType type = ConfigType.CLIENT;

        public <T> Builder(String modID, String translation, Class<T> enumClass, T defaultValue) {
            this.defaultValue = (Enum<?>) defaultValue;
            this.translation = translation;
            this.enumClass = enumClass;
            this.modID = modID;
        }

        public Builder descriptor(Object descriptor) { this.descriptor = descriptor; return this; }
        public Builder requiresRestart(boolean requiresRestart) { this.requiresRestart = requiresRestart; return this; }
        public Builder hoverColor(int hoverColor) { this.hoverColor = hoverColor; return this; }
        public Builder priority(int priority) { this.priority = priority; return this; }
        public Builder divider(DividerEntry divider) { this.divider = divider; return this; }
        public Builder image(String path, int width, int height) { this.image = new ImageComponent(this.modID, path, width, height); return this; }
        public Builder image(String path, int width, int height, int frameHeight, int ticks) { this.image = new ImageComponent(this.modID, path, width, height, frameHeight, ticks); return this; }
        public Builder type(ConfigType type) { this.type = type; return this; }

        public EnumEntry build() {
            EnumEntry entry = new EnumEntry();
            entry.modID = modID;
            entry.value = defaultValue.name();
            entry.modID = modID;
            entry.translation = translation;
            entry.enumClass = enumClass;
            entry.descriptor = descriptor;
            entry.image = image;
            entry.requiresRestart = requiresRestart;
            entry.hoverColor = hoverColor;
            entry.priority = priority;
            entry.defaultValue = defaultValue.name();
            entry.divider = divider;
            entry.type = type;

            Enum<?> configValue = (Enum<?>) CCFile.getInstance().getConfigValue(modID, translation, enumClass);

            boolean validated = ConfigValidity.validateEnumConfig(configValue != null ? configValue.toString() : String.valueOf(defaultValue), enumClass);

            if (configValue != null && validated) entry.value = configValue.name();
            else if (!validated) Constants.LOGGER.error("Failed to validate config value for {}:{}. Using default value: {}", modID, translation, defaultValue);

            CCEntries.addEntry(entry.modID(), entry);
            return entry;
        }
    }

}
