package com.pryzmm.coreconfigapi.entry;

import com.pryzmm.coreconfigapi.component.ImageComponent;
import com.pryzmm.coreconfigapi.data.CCEntries;
import com.pryzmm.coreconfigapi.data.ConfigType;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.NotNull;

public class WebsiteEntry implements MainEntry {

    private String value;
    private MutableComponent descriptor;
    private ImageComponent image;
    private String translation;
    private String modID;
    private Integer hoverColor;
    private int priority;
    private DividerEntry divider;

    private WebsiteEntry() {}

    public MutableComponent descriptor() { return descriptor; }
    public ImageComponent image() { return image; }
    public boolean requiresRestart() { return false; }
    public String translation() { return translation; }
    public String modID() { return modID; }
    public Integer hoverColor() { return hoverColor; }
    public int priority() { return priority; }
    public DividerEntry divider() { return divider; }
    public ConfigType type() { return ConfigType.CLIENT; }

    public String getUnsavedValue() { return value; }
    public String getDefaultValue() { return value; }
    public String getValue() {
        return value;
    }
    public String getClientValue() {
        return value;
    }
    public String getServerValue() {
        return value;
    }
    public void setValue(String value) { this.value = value; }

    public void refreshValue() {}
    public void save() {}

    public static class Builder {
        private final String website;
        private final String translation;
        private final String modID;
        private MutableComponent descriptor = Component.empty();
        private ImageComponent image = null;
        private Integer hoverColor = null;
        private int priority = 0;
        private DividerEntry divider = null;

        public Builder(String modID, String translation, @NotNull String website) {
            this.website = website;
            this.translation = translation;
            this.modID = modID;
        }

        public Builder descriptor(MutableComponent descriptor) { this.descriptor = descriptor; return this; }
        public Builder hoverColor(int hoverColor) { this.hoverColor = hoverColor; return this; }
        public Builder priority(int priority) { this.priority = priority; return this; }
        public Builder divider(DividerEntry divider) { this.divider = divider; return this; }
        public Builder image(String path, int width, int height) { this.image = new ImageComponent(this.modID, path, width, height); return this; }
        public Builder image(String path, int width, int height, int frameHeight, int ticks) { this.image = new ImageComponent(this.modID, path, width, height, frameHeight, ticks); return this; }

        public WebsiteEntry build() {
            WebsiteEntry entry = new WebsiteEntry();
            entry.value = website;
            entry.translation = translation;
            entry.modID = modID;
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
