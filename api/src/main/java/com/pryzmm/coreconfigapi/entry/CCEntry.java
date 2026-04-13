package com.pryzmm.coreconfigapi.entry;

import org.jetbrains.annotations.ApiStatus;

/**
 * Interface used to connect all entries together
 */
@ApiStatus.Internal
public interface CCEntry {

    int priority();
    String translation();

}