package com.pryzmm.coreconfigapi.entry;

import net.minecraft.resources.Identifier;

public interface CategoryEntry extends CCEntry {

    Identifier identifier();
    Object getValue();
    Integer textColor();
    int priority();
}