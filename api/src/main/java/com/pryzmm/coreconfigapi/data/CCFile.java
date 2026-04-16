package com.pryzmm.coreconfigapi.data;

import com.pryzmm.coreconfigapi.Constants;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public abstract class CCFile {

    @ApiStatus.Internal
    private static CCFile instance = new CCFile() {
        @Override
        public <T> T getConfigValue(String modName, String translation, Class<T> clazz) {
            Constants.LOGGER.error("Tried to get a config value but the instance hasn't been initialized yet!");
            return null;
        }

        @Override
        public <T> T getServerValue(String modName, String translation, Class<T> clazz) {
            Constants.LOGGER.error("Tried to get a server config value but the instance hasn't been initialized yet!");
            return null;
        }
    };

    @ApiStatus.Internal
    public static void setInstance(CCFile newInstance) {
        instance = newInstance;
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