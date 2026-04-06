package com.pryzmm.coreconfigapi.entry;

import com.pryzmm.coreconfigapi.component.ImageComponent;
import com.pryzmm.coreconfigapi.data.CCEntries;
import com.pryzmm.coreconfigapi.data.CCFile;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.NotNull;

public class WebsiteEntry implements MainEntry {

    private String value;
    private MutableComponent descriptor;
    private ImageComponent image;
    private Identifier identifier;
    private Integer hoverColor;
    private int priority;
    private DividerEntry divider;

    private WebsiteEntry() {}

    public MutableComponent descriptor() { return descriptor; }
    public ImageComponent image() { return image; }
    public boolean requiresRestart() { return false; }
    public Identifier identifier() { return identifier; }
    public Integer hoverColor() { return hoverColor; }
    public int priority() { return priority; }
    public DividerEntry divider() { return divider; }

    public String getUnsavedValue() { return value; }
    public String getDefaultValue() { return value; }
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public void refreshValue() {}
    public void save() {}

    public static class Builder {
        private final String website;
        private final Identifier identifier;
        private MutableComponent descriptor = Component.empty();
        private ImageComponent image = null;
        private Integer hoverColor = null;
        private int priority = 0;
        private DividerEntry divider = null;

        public Builder(Identifier identifier, @NotNull String website) {
            this.website = website;
            this.identifier = identifier;
        }

        public Builder descriptor(MutableComponent descriptor) { this.descriptor = descriptor; return this; }
        public Builder hoverColor(int hoverColor) { this.hoverColor = hoverColor; return this; }
        public Builder priority(int priority) { this.priority = priority; return this; }
        public Builder divider(DividerEntry divider) { this.divider = divider; return this; }
        public Builder image(Identifier identifier, int width, int height) { this.image = new ImageComponent(identifier, width, height); return this; }

        public WebsiteEntry build() {
            WebsiteEntry entry = new WebsiteEntry();
            entry.value = website;
            entry.identifier = identifier;
            entry.descriptor = descriptor;
            entry.image = image;
            entry.hoverColor = hoverColor;
            entry.priority = priority;
            entry.divider = divider;

            String configValue = CCFile.getInstance().getConfigValue(identifier, String.class);
            if (configValue != null) entry.value = configValue;

            CCEntries.addEntry(entry.identifier.getNamespace(), entry);
            return entry;
        }
    }
}
