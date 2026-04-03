package com.pryzmm.coreconfig.entry;

import com.pryzmm.coreconfig.data.CCFileHandler;
import com.pryzmm.coreconfig.data.EntryHolder;
import com.pryzmm.coreconfig.ui.objects.ImageComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class IntegerEntry implements CCEntry {

    private Integer value;
    private Integer defaultValue;
    private Object newValue = null;
    private Component descriptor;
    private ImageComponent image;
    private Identifier identifier;
    private Integer hoverColor;
    private boolean requiresRestart;
    private int minimum = Integer.MIN_VALUE;
    private int maximum = Integer.MAX_VALUE;
    private int priority = 0;

    private IntegerEntry() {}

    public Component descriptor() { return descriptor; }
    public ImageComponent image() { return image; }
    public boolean requiresRestart() { return requiresRestart; }
    public Identifier identifier() { return identifier; }
    public Integer hoverColor() { return hoverColor; }
    public int priority() { return priority; }
    public Integer minimum() { return minimum; }
    public Integer maximum() { return maximum; }

    public Object getUnsavedValue() { return newValue != null ? newValue : value; }
    public Integer getDefaultValue() { return defaultValue; }
    public Integer getValue() { return value; }
    public void setValue(Integer value) { this.value = value; }

    public void refreshValue() { this.newValue = value; }

    public void change(Object newValue) { this.newValue = newValue; }

    public void save() {
        if (newValue != null) {
            if (!requiresRestart) setValue((Integer) newValue);
        }
    }

    public static class Builder {
        private final Integer defaultValue;
        private final Identifier identifier;
        private Component descriptor = Component.empty();
        private ImageComponent image = null;
        private boolean requiresRestart = false;
        private Integer hoverColor = null;
        private int minimum = Integer.MIN_VALUE;
        private int maximum = Integer.MAX_VALUE;
        private int priority = 0;

        public Builder(Identifier identifier, Integer defaultValue) {
            this.defaultValue = defaultValue == null ? 0 : defaultValue;
            this.identifier = identifier;
        }

        public Builder descriptor(Component descriptor) { this.descriptor = descriptor; return this; }
        public Builder requiresRestart(boolean requiresRestart) { this.requiresRestart = requiresRestart; return this; }
        public Builder hoverColor(int hoverColor) { this.hoverColor = hoverColor; return this; }
        public Builder minimum(int minimum) { this.minimum = minimum; return this; }
        public Builder maximum(int maximum) { this.maximum = maximum; return this; }
        public Builder priority(int priority) { this.priority = priority; return this; }
        public Builder image(Identifier identifier, int width, int height) { this.image = new ImageComponent(identifier, width, height); return this; }

        public IntegerEntry build() {
            IntegerEntry entry = new IntegerEntry();
            entry.value = defaultValue;
            entry.identifier = identifier;
            entry.descriptor = descriptor;
            entry.requiresRestart = requiresRestart;
            entry.hoverColor = hoverColor;
            entry.minimum = minimum;
            entry.maximum = maximum;
            entry.priority = priority;
            entry.defaultValue = defaultValue;
            entry.image = image;

            Integer configValue = CCFileHandler.getConfigValue(identifier, Integer.class);
            if (configValue != null) entry.value = configValue;

            EntryHolder.addEntry(entry.identifier.getNamespace(), entry);
            return entry;
        }
    }
}
