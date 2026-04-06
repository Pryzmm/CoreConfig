package com.pryzmm.coreconfigapi.data;

public abstract class CCFile {

    private static CCFile instance = new CCFile() {
        @Override
        public <T> T getConfigValue(String modName, String translation, Class<T> clazz) {
            return null;
        }
    };

    public static void setInstance(CCFile file) {
        instance = file;
    }

    public static CCFile getInstance() {
        return instance;
    }

    public abstract <T> T getConfigValue(String modName, String translation, Class<T> clazz);
}