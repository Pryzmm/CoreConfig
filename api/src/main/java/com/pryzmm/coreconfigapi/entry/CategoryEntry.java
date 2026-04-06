package com.pryzmm.coreconfigapi.entry;

public interface CategoryEntry extends CCEntry {

    String modID();
    String translation();
    Object getValue();
    Integer textColor();
    int priority();
}