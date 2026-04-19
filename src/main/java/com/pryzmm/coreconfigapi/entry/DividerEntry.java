package com.pryzmm.coreconfigapi.entry;

import com.pryzmm.coreconfigapi.data.CCEntries;

/**
 * A configuration option for CoreConfig, dedicated towards creating dividers for sorting configuration options. Use the {@link Builder} to create a new DividerEntry for use in mods.
 * <p>
 * Visit <a href="https://github.com/Pryzmm/CoreConfig/wiki/Entries#dividerentry">the wiki page</a> to view more detailed documentation regarding this entry and its builder methods.
 */
public class DividerEntry implements CategoryEntry {

    private boolean value;
    private String translation;
    private String modID;
    private Integer textColor;
    private int priority;
    private boolean isFolded;

    private DividerEntry() {}

    public String translation() { return translation; }
    public String modID() { return modID; }
    public Integer textColor() { return textColor; }
    public int priority() { return priority; }
    public boolean getFoldedState() { return isFolded; }
    public void setFoldedState(boolean isFolded) { this.isFolded = isFolded; }

    public Boolean getValue() { return value; }
    public void setValue(boolean value) { this.value = value; }

    public static class Builder {
        private final String translation;
        private final String modID;
        private Integer textColor = null;
        private int priority = 0;
        private boolean isFolded = false;

        public Builder(String modID, String translation) {
            this.translation = translation;
            this.modID = modID;
        }

        public Builder textColor(int textColor) { this.textColor = textColor; return this; }
        public Builder priority(int priority) { this.priority = priority; return this; }
        public Builder initiallyFolded(boolean isFolded) { this.isFolded = isFolded; return this; };

        public DividerEntry build() {
            DividerEntry entry = new DividerEntry();
            entry.modID = modID;
            entry.translation = translation;
            entry.textColor = textColor;
            entry.priority = priority;
            entry.isFolded = isFolded;

            CCEntries.addEntry(entry.modID(), entry);
            return entry;
        }
    }

}
