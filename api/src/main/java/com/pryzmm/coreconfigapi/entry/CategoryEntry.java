package com.pryzmm.coreconfigapi.entry;

import org.jetbrains.annotations.ApiStatus;

/**
 * Interface used for dividers.
 */
@ApiStatus.Internal
public interface CategoryEntry extends CCEntry {

    String modID();
    String translation();
    Object getValue();
    Integer textColor();
    int priority();
}