package com.pryzmm.coreconfigapi.component;

public record ImageComponent(String modID, String path, int imageWidth, int imageHeight, int frameHeight, int animationTime) {

    public ImageComponent(String modID, String path, int imageWidth, int imageHeight) {
        this(modID, path, imageWidth, imageHeight, imageHeight, -1);
    }

}
