package com.pryzmm.coreconfigapi.entry;

import com.pryzmm.coreconfigapi.component.ImageComponent;
import com.pryzmm.coreconfigapi.data.CCEntries;
import com.pryzmm.coreconfigapi.data.CCFile;
import net.minecraft.network.chat.Component;

public class IntegerEntry implements MainEntry {

    private Integer value;
    private Integer defaultValue;
    private String newValue = null;
    private Component descriptor;
    private ImageComponent image;
    private String translation;
    private String modID;
    private Integer hoverColor;
    private boolean requiresRestart;
    private int minimum;
    private int maximum;
    private int priority = 0;
    private DividerEntry divider;

    private IntegerEntry() {}

    public Component descriptor() { return descriptor; }
    public ImageComponent image() { return image; }
    public boolean requiresRestart() { return requiresRestart; }
    public String translation() { return translation; }
    public String modID() { return modID; }
    public Integer hoverColor() { return hoverColor; }
    public int priority() { return priority; }
    public Integer minimum() { return minimum; }
    public Integer maximum() { return maximum; }
    public DividerEntry divider() { return divider; }

    public Object getUnsavedValue() { return newValue != null ? newValue : value; }
    public Integer getDefaultValue() { return defaultValue; }
    public Integer getValue() { return value; }
    public void setValue(Integer value) { this.value = value; }

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
            if (!requiresRestart) setValue(Integer.valueOf(newValue));
        }
    }

    public static class Builder {
        private final Integer defaultValue;
        private final String translation;
        private final String modID;
        private Component descriptor = Component.empty();
        private ImageComponent image = null;
        private boolean requiresRestart = false;
        private Integer hoverColor = null;
        private int minimum = Integer.MIN_VALUE;
        private int maximum = Integer.MAX_VALUE;
        private int priority = 0;
        private DividerEntry divider = null;

        public Builder(String modID, String translation, Integer defaultValue) {
            this.defaultValue = defaultValue == null ? 0 : defaultValue;
            this.translation = translation;
            this.modID = modID;
        }

        public Builder descriptor(Component descriptor) { this.descriptor = descriptor; return this; }
        public Builder requiresRestart(boolean requiresRestart) { this.requiresRestart = requiresRestart; return this; }
        public Builder hoverColor(int hoverColor) { this.hoverColor = hoverColor; return this; }
        public Builder minimum(int minimum) { this.minimum = minimum; return this; }
        public Builder maximum(int maximum) { this.maximum = maximum; return this; }
        public Builder priority(int priority) { this.priority = priority; return this; }
        public Builder divider(DividerEntry divider) { this.divider = divider; return this; }
        public Builder image(String path, int width, int height) { this.image = new ImageComponent(this.modID, path, width, height); return this; }

        public IntegerEntry build() {
            IntegerEntry entry = new IntegerEntry();
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

            Integer configValue = CCFile.getInstance().getConfigValue(modID, translation, Integer.class);
            if (configValue != null) entry.value = configValue;

            CCEntries.addEntry(entry.modID(), entry);
            return entry;
        }
    }
}
