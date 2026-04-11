package com.pryzmm.coreconfig;

import com.pryzmm.coreconfig.data.CCFileHandler;
import com.pryzmm.coreconfig.services.Services;
import com.pryzmm.coreconfigapi.data.CCFile;

public class CoreConfigCommon {

    public static void initFirst() {
        CCFile.setInstance(new CCFileHandler());
    }

    public static void init() {
        Services.NETWORK.registerPayloads();
    }

}
