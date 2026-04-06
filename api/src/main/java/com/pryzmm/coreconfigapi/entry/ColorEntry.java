package com.pryzmm.coreconfigapi.entry;

import com.pryzmm.coreconfigapi.component.ImageComponent;
import com.pryzmm.coreconfigapi.data.CCEntries;
import com.pryzmm.coreconfigapi.data.CCFile;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class ColorEntry implements MainEntry {

    private Integer value;
    private Integer defaultValue;
    private Integer newValue = null;
    private Component descriptor;
    private ImageComponent image;
    private Identifier identifier;
    private Integer hoverColor;
    private boolean requiresRestart;
    private int priority;
    private boolean allowAlpha;
    private DividerEntry divider;

    private ColorEntry() {}

    public Component descriptor() { return descriptor; }
    public ImageComponent image() { return image; }
    public boolean requiresRestart() { return requiresRestart; }
    public Identifier identifier() { return identifier; }
    public Integer hoverColor() { return hoverColor; }
    public int priority() { return priority; }
    public DividerEntry divider() { return divider; }
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
        private final Integer defaultValue;
        private final Identifier identifier;
        private Component descriptor = Component.empty();
        private ImageComponent image = null;
        private boolean requiresRestart = false;
        private Integer hoverColor = null;
        private int priority = 0;
        private DividerEntry divider = null;
        private boolean allowAlpha = false;

        public Builder(Identifier identifier, Integer defaultValue) {
            this.defaultValue = defaultValue == null ? 0 : defaultValue;
            this.identifier = identifier;
        }

        public Builder descriptor(Component descriptor) { this.descriptor = descriptor; return this; }
        public Builder requiresRestart(boolean requiresRestart) { this.requiresRestart = requiresRestart; return this; }
        public Builder hoverColor(int hoverColor) { this.hoverColor = hoverColor; return this; }
        public Builder priority(int priority) { this.priority = priority; return this; }
        public Builder divider(DividerEntry divider) { this.divider = divider; return this; }
        public Builder allowAlpha(boolean allowAlpha) { this.allowAlpha = allowAlpha; return this; }
        public Builder image(Identifier identifier, int width, int height) { this.image = new ImageComponent(identifier, width, height); return this; }

        public ColorEntry build() {
            ColorEntry entry = new ColorEntry();
            entry.value = defaultValue;
            entry.identifier = identifier;
            entry.descriptor = descriptor;
            entry.requiresRestart = requiresRestart;
            entry.hoverColor = hoverColor;
            entry.priority = priority;
            entry.defaultValue = defaultValue;
            entry.image = image;
            entry.divider = divider;
            entry.allowAlpha = allowAlpha;

            Integer configValue = CCFile.getInstance().getConfigValue(identifier, Integer.class);
            if (configValue != null) entry.value = configValue;

            CCEntries.addEntry(entry.identifier.getNamespace(), entry);
            return entry;
        }
    }
}
