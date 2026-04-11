package com.pryzmm.coreconfigapi.entry;

import com.pryzmm.coreconfigapi.component.ImageComponent;
import com.pryzmm.coreconfigapi.data.CCEntries;
import com.pryzmm.coreconfigapi.data.ConfigType;


/**
 * A configuration option for CoreConfig, dedicated towards running custom code when the button is clicked. Use the {@link Builder} to create a new CustomEntry for use in mods.
 * <p>
 * Visit <a href="https://github.com/Pryzmm/CoreConfig/wiki/Entries#customentry">the wiki page</a> to view more detailed documentation regarding this entry and its builder methods.
 */
public class CustomEntry implements MainEntry {

    private Runnable runnable;
    private Object descriptor;
    private ImageComponent image;
    private String translation;
    private String modID;
    private Integer hoverColor;
    private int priority;
    private DividerEntry divider;
    private ConfigType type;

    private CustomEntry() {}

    public Object descriptor() { return descriptor; }
    public ImageComponent image() { return image; }
    public boolean requiresRestart() { return false; }
    public String translation() { return translation; }
    public String modID() { return modID; }
    public Integer hoverColor() { return hoverColor; }
    public int priority() { return priority; }
    public DividerEntry divider() { return divider; }
    public ConfigType type() { return type; }

    public Runnable getUnsavedValue() { return runnable; }
    public Runnable getDefaultValue() { return runnable; }
    public Runnable getValue() { return runnable; }
    public Runnable getClientValue() { return runnable; }
    public Runnable getServerValue() { return runnable; }

    public void setValue(Runnable runnable) { this.runnable = runnable; }

    public void refreshValue() {}
    public void save() {}

    public static class Builder {
        private final String modID;
        private final String translation;
        private Runnable runnable;
        private Object descriptor;
        private ImageComponent image = null;
        private Integer hoverColor = null;
        private int priority = 0;
        private DividerEntry divider = null;
        private ConfigType type = ConfigType.CLIENT;

        public Builder(String modID, String translation) {
            this.modID = modID;
            this.translation = translation;
        }

        public Builder runnable(Runnable runnable) { this.runnable = runnable; return this; }
        public Builder descriptor(Object descriptor) { this.descriptor = descriptor; return this; }
        public Builder hoverColor(int hoverColor) { this.hoverColor = hoverColor; return this; }
        public Builder priority(int priority) { this.priority = priority; return this; }
        public Builder divider(DividerEntry divider) { this.divider = divider; return this; }
        public Builder image(String path, int width, int height) { this.image = new ImageComponent(this.modID, path, width, height); return this; }
        public Builder image(String path, int width, int height, int frameHeight, int ticks) { this.image = new ImageComponent(this.modID, path, width, height, frameHeight, ticks); return this; }
        public Builder type(ConfigType type) { this.type = type; return this; }

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
            entry.type = type;

            CCEntries.addEntry(entry.modID(), entry);
            return entry;
        }
    }
}
