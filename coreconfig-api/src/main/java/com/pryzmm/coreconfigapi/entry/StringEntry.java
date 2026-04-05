package com.pryzmm.coreconfigapi.entry;

import com.pryzmm.coreconfigapi.component.ImageComponent;
import com.pryzmm.coreconfigapi.data.CCEntries;
import com.pryzmm.coreconfigapi.data.CCFile;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;

public class StringEntry implements MainEntry {

    private String value;
    private String defaultValue;
    private String newValue = null;
    private MutableComponent descriptor;
    private ImageComponent image;
    private Identifier identifier;
    private Integer hoverColor;
    private boolean requiresRestart;
    private int minimumLength;
    private int maximumLength;
    private int priority;
    private DividerEntry divider;

    private StringEntry() {}

    public MutableComponent descriptor() { return descriptor; }
    public ImageComponent image() { return image; }
    public boolean requiresRestart() { return requiresRestart; }
    public Identifier identifier() { return identifier; }
    public Integer hoverColor() { return hoverColor; }
    public int priority() { return priority; }
    public Integer minimumLength() { return minimumLength; }
    public Integer maximumLength() { return maximumLength; }
    public DividerEntry divider() { return divider; }

    public String getUnsavedValue() { return newValue != null ? newValue : value; }
    public String getDefaultValue() { return defaultValue; }
    public String getValue() { return value; }
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
        private final Identifier identifier;
        private MutableComponent descriptor = Component.empty();
        private ImageComponent image = null;
        private boolean requiresRestart = false;
        private Integer hoverColor = null;
        private int minimumLength = 0;
        private int maximumLength = Integer.MAX_VALUE;
        private int priority = 0;
        private DividerEntry divider = null;

        public Builder(Identifier identifier, String defaultValue) {
            this.defaultValue = defaultValue == null ? "" : defaultValue;
            this.identifier = identifier;
        }

        public Builder descriptor(MutableComponent descriptor) { this.descriptor = descriptor; return this; }
        public Builder requiresRestart(boolean requiresRestart) { this.requiresRestart = requiresRestart; return this; }
        public Builder hoverColor(int hoverColor) { this.hoverColor = hoverColor; return this; }
        public Builder minimumLength(int minimumLength) { this.minimumLength = minimumLength; return this; }
        public Builder maximumLength(int maximumLength) { this.maximumLength = maximumLength; return this; }
        public Builder priority(int priority) { this.priority = priority; return this; }
        public Builder divider(DividerEntry divider) { this.divider = divider; return this; }
        public Builder image(Identifier identifier, int width, int height) { this.image = new ImageComponent(identifier, width, height); return this; }

        public StringEntry build() {
            StringEntry entry = new StringEntry();
            entry.value = defaultValue;
            entry.identifier = identifier;
            entry.descriptor = descriptor;
            entry.image = image;
            entry.requiresRestart = requiresRestart;
            entry.hoverColor = hoverColor;
            entry.minimumLength = minimumLength;
            entry.maximumLength = maximumLength;
            entry.priority = priority;
            entry.defaultValue = defaultValue;
            entry.divider = divider;

            String configValue = CCFile.getInstance().getConfigValue(identifier, String.class);
            if (configValue != null) entry.value = configValue;

            CCEntries.addEntry(entry.identifier.getNamespace(), entry);
            return entry;
        }
    }
}
