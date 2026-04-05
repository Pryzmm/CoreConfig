package com.pryzmm.coreconfigapi.entry;

import com.pryzmm.coreconfigapi.data.CCEntries;
import com.pryzmm.coreconfigapi.data.CCFile;
import net.minecraft.resources.Identifier;

public class DividerEntry implements CategoryEntry {

    private boolean value;
    private Identifier identifier;
    private Integer textColor;
    private int priority;

    private DividerEntry() {}

    public Identifier identifier() { return identifier; }
    public Integer textColor() { return textColor; }
    public int priority() { return priority; }

    public Boolean getValue() { return value; }
    public void setValue(boolean value) { this.value = value; }

    public static class Builder {
        private final Identifier identifier;
        private Integer textColor = null;
        private int priority = 0;

        public Builder(Identifier identifier) {
            this.identifier = identifier;
        }

        public Builder textColor(int textColor) { this.textColor = textColor; return this; }
        public Builder priority(int priority) { this.priority = priority; return this; }

        public DividerEntry build() {
            DividerEntry entry = new DividerEntry();
            entry.identifier = identifier;
            entry.textColor = textColor;
            entry.priority = priority;

            Boolean configValue = CCFile.getInstance().getConfigValue(identifier, Boolean.class);
            if (configValue != null) entry.value = configValue;

            CCEntries.addEntry(entry.identifier.getNamespace(), entry);
            return entry;
        }
    }

}
