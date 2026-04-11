package com.pryzmm.coreconfigapi.data;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public abstract class CCFile {

    @ApiStatus.Internal
    private static CCFile instance = new CCFile() {
        @Override
        public <T> T getConfigValue(String modName, String translation, Class<T> clazz) {
            return null;
        }

        @Override
        public <T> T getServerValue(String modName, String translation, Class<T> clazz) {
            return null;
        }
    };

    @ApiStatus.Internal
    public static void setInstance(CCFile file) {
        instance = file;
    }

    @ApiStatus.Internal
    public static CCFile getInstance() {
        return instance;
    }

    @ApiStatus.Internal
    public abstract <T> T getConfigValue(String modName, String translation, Class<T> clazz);

    @ApiStatus.Internal
    public abstract <T> T getServerValue(String modName, String translation, Class<T> clazz);
}