package com.pryzmm.coreconfigapi.component;

/**
 * Record for creating an image component for entry builders.
 * @param modID The ID of the mod
 * @param path The path to the image
 * @param imageWidth The width of the image
 * @param imageHeight The height of the image
 * @param frameHeight The height of a single frame in an amination
 * @param animationTime The amount of time in ticks before the next frame appears
 */
public record ImageComponent(String modID, String path, int imageWidth, int imageHeight, int frameHeight, int animationTime) {

    /**
     * Record for creating an image component for entry builders.
     * @param modID The ID of the mod
     * @param path The path to the image
     * @param imageWidth The width of the image
     * @param imageHeight The height of the image
     */
    public ImageComponent(String modID, String path, int imageWidth, int imageHeight) {
        this(modID, path, imageWidth, imageHeight, imageHeight, -1);
    }

}
