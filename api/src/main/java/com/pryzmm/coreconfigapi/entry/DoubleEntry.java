package com.pryzmm.coreconfigapi.entry;

import com.pryzmm.coreconfigapi.component.ImageComponent;
import com.pryzmm.coreconfigapi.data.CCEntries;
import com.pryzmm.coreconfigapi.data.CCFile;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class DoubleEntry implements MainEntry {

    private Double value;
    private Double defaultValue;
    private String newValue = null;
    private Component descriptor;
    private ImageComponent image;
    private Identifier identifier;
    private Integer hoverColor;
    private boolean requiresRestart;
    private double minimum;
    private double maximum;
    private int priority;
    private DividerEntry divider;

    private DoubleEntry() {}

    public Component descriptor() { return descriptor; }
    public ImageComponent image() { return image; }
    public boolean requiresRestart() { return requiresRestart; }
    public Identifier identifier() { return identifier; }
    public Integer hoverColor() { return hoverColor; }
    public int priority() { return priority; }
    public Double minimum() { return minimum; }
    public Double maximum() { return maximum; }
    public DividerEntry divider() { return divider; }

    public Object getUnsavedValue() { return newValue != null ? newValue : value; }
    public Double getDefaultValue() { return defaultValue; }
    public Double getValue() { return value; }
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
        private final Identifier identifier;
        private Component descriptor = Component.empty();
        private ImageComponent image = null;
        private boolean requiresRestart = false;
        private Integer hoverColor = null;
        private double minimum = -Double.MAX_VALUE;
        private double maximum = Double.MAX_VALUE;
        private int priority = 0;
        private DividerEntry divider = null;

        public Builder(Identifier identifier, Double defaultValue) {
            this.defaultValue = defaultValue == null ? 0 : defaultValue;
            this.identifier = identifier;
        }

        public Builder descriptor(Component descriptor) { this.descriptor = descriptor; return this; }
        public Builder requiresRestart(boolean requiresRestart) { this.requiresRestart = requiresRestart; return this; }
        public Builder hoverColor(int hoverColor) { this.hoverColor = hoverColor; return this; }
        public Builder minimum(float minimum) { this.minimum = minimum; return this; }
        public Builder maximum(float maximum) { this.maximum = maximum; return this; }
        public Builder priority(int priority) { this.priority = priority; return this; }
        public Builder divider(DividerEntry divider) { this.divider = divider; return this; }
        public Builder image(Identifier identifier, int width, int height) { this.image = new ImageComponent(identifier, width, height); return this; }

        public DoubleEntry build() {
            DoubleEntry entry = new DoubleEntry();
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
            entry.divider = divider;

            Double configValue = CCFile.getInstance().getConfigValue(identifier, Double.class);
            if (configValue != null) entry.value = configValue;

            CCEntries.addEntry(entry.identifier.getNamespace(), entry);
            return entry;
        }
    }
}
