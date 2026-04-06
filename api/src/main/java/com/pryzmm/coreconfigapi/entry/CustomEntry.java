package com.pryzmm.coreconfigapi.entry;

import com.pryzmm.coreconfigapi.component.ImageComponent;
import com.pryzmm.coreconfigapi.data.CCEntries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class CustomEntry implements MainEntry {

    private Runnable runnable;
    private MutableComponent descriptor;
    private ImageComponent image;
    private String translation;
    private String modID;
    private Integer hoverColor;
    private int priority;
    private DividerEntry divider;

    private CustomEntry() {}

    public MutableComponent descriptor() { return descriptor; }
    public ImageComponent image() { return image; }
    public boolean requiresRestart() { return false; }
    public String translation() { return translation; }
    public String modID() { return modID; }
    public Integer hoverColor() { return hoverColor; }
    public int priority() { return priority; }
    public DividerEntry divider() { return divider; }

    public Runnable getUnsavedValue() { return runnable; }
    public Runnable getDefaultValue() { return runnable; }
    public Runnable getValue() { return runnable; }
    public void setValue(Runnable runnable) { this.runnable = runnable; }

    public void refreshValue() {}
    public void save() {}

    public static class Builder {
        private final String modID;
        private final String translation;
        private Runnable runnable;
        private MutableComponent descriptor = Component.empty();
        private ImageComponent image = null;
        private Integer hoverColor = null;
        private int priority = 0;
        private DividerEntry divider = null;

        public Builder(String modID, String translation) {
            this.modID = modID;
            this.translation = translation;
        }

        public Builder runnable(Runnable runnable) { this.runnable = runnable; return this; }
        public Builder descriptor(MutableComponent descriptor) { this.descriptor = descriptor; return this; }
        public Builder hoverColor(int hoverColor) { this.hoverColor = hoverColor; return this; }
        public Builder priority(int priority) { this.priority = priority; return this; }
        public Builder divider(DividerEntry divider) { this.divider = divider; return this; }
        public Builder image(String path, int width, int height) { this.image = new ImageComponent(this.modID, path, width, height); return this; }

        public CustomEntry build() {
            CustomEntry entry = new CustomEntry();
            entry.runnable = runnable;
            entry.modID = modID;
            entry.translation = translation;
            entry.descriptor = descriptor;
            entry.image = image;
            entry.hoverColor = hoverColor;
            entry.priority = priority;
            entry.divider = divider;

            CCEntries.addEntry(entry.modID(), entry);
            return entry;
        }
    }
}
