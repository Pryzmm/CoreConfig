package com.pryzmm.coreconfigapi.entry;

import com.pryzmm.coreconfigapi.component.ImageComponent;
import com.pryzmm.coreconfigapi.data.CCFileHandler;
import com.pryzmm.coreconfigapi.data.EntryHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class BooleanEntry implements MainEntry {

    private boolean value;
    private boolean defaultValue;
    private Boolean newValue = null;
    private Component descriptor;
    private ImageComponent image;
    private Identifier identifier;
    private Integer hoverColor;
    private boolean requiresRestart;
    private int priority;

    private BooleanEntry() {}

    public Component descriptor() { return descriptor; }
    public boolean requiresRestart() { return requiresRestart; }
    public Identifier identifier() { return identifier; }
    public Integer hoverColor() { return hoverColor; }
    public int priority() { return priority; }
    public ImageComponent image() { return image; }

    public Boolean getUnsavedValue() { return newValue != null ? newValue : value; }
    public Boolean getDefaultValue() { return defaultValue; }
    public Boolean getValue() { return value; }
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
        private final Identifier identifier;
        private Component descriptor = Component.empty();
        private ImageComponent image = null;
        private boolean requiresRestart = false;
        private Integer hoverColor = null;
        private int priority = 0;

        public Builder(Identifier identifier, boolean defaultValue) {
            this.defaultValue = defaultValue;
            this.identifier = identifier;
        }

        public Builder descriptor(Component descriptor) { this.descriptor = descriptor; return this; }
        public Builder requiresRestart(boolean requiresRestart) { this.requiresRestart = requiresRestart; return this; }
        public Builder hoverColor(int hoverColor) { this.hoverColor = hoverColor; return this; }
        public Builder priority(int priority) { this.priority = priority; return this; }
        public Builder image(Identifier identifier, int width, int height) { this.image = new ImageComponent(identifier, width, height); return this; }

        public BooleanEntry build() {
            BooleanEntry entry = new BooleanEntry();
            entry.value = defaultValue;
            entry.identifier = identifier;
            entry.descriptor = descriptor;
            entry.image = image;
            entry.requiresRestart = requiresRestart;
            entry.hoverColor = hoverColor;
            entry.priority = priority;
            entry.defaultValue = defaultValue;

            Boolean configValue = CCFileHandler.getConfigValue(identifier, Boolean.class);
            if (configValue != null) entry.value = configValue;

            EntryHolder.addEntry(entry.identifier.getNamespace(), entry);
            return entry;
        }
    }
}
