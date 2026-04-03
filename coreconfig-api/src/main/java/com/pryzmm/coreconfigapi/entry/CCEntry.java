package com.pryzmm.coreconfigapi.entry;

import net.fabricmc.loader.api.FabricLoader;
import java.util.function.Supplier;

@SuppressWarnings("ClassCanBeRecord")
public class CCEntry<T> {
    private final MainEntry entry;
    private final T defaultValue;

    private CCEntry(MainEntry entry, T defaultValue) {
        this.entry = entry;
        this.defaultValue = defaultValue;
    }

    @SuppressWarnings("unchecked")
    public T get() {
        return entry != null ? (T) entry.getValue() : defaultValue;
    }

    public static <T> CCEntry<T> of(Supplier<MainEntry> builder, T defaultValue) {
        if (FabricLoader.getInstance().isModLoaded("coreconfig")) {
            return new CCEntry<>(builder.get(), defaultValue);
        }
        return new CCEntry<>(null, defaultValue);
    }
}