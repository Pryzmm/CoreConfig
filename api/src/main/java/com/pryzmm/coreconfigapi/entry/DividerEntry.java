package com.pryzmm.coreconfigapi.entry;

import com.pryzmm.coreconfigapi.data.CCEntries;

public class DividerEntry implements CategoryEntry {

    private boolean value;
    private String translation;
    private String modID;
    private Integer textColor;
    private int priority;

    private DividerEntry() {}

    public String translation() { return translation; }
    public String modID() { return modID; }
    public Integer textColor() { return textColor; }
    public int priority() { return priority; }

    public Boolean getValue() { return value; }
    public void setValue(boolean value) { this.value = value; }

    public static class Builder {
        private final String translation;
        private final String modID;
        private Integer textColor = null;
        private int priority = 0;

        public Builder(String modID, String translation) {
            this.translation = translation;
            this.modID = modID;
        }

        public Builder textColor(int textColor) { this.textColor = textColor; return this; }
        public Builder priority(int priority) { this.priority = priority; return this; }

        public DividerEntry build() {
            DividerEntry entry = new DividerEntry();
            entry.modID = modID;
            entry.translation = translation;
            entry.textColor = textColor;
            entry.priority = priority;

            CCEntries.addEntry(entry.modID(), entry);
            return entry;
        }
    }

}
