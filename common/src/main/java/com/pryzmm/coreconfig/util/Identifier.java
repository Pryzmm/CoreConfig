package com.pryzmm.coreconfig.util;

public class Identifier {

    public static net.minecraft.resources.Identifier get(String modID, String path) {
        return net.minecraft.resources.Identifier.fromNamespaceAndPath(modID, path);
    }

}
