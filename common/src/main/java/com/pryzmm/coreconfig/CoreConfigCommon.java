package com.pryzmm.coreconfig;

import com.pryzmm.coreconfig.services.Services;

public class CoreConfigCommon {

    public static void init() {
        Services.NETWORK.registerPayloads();
    }

}
