package com.YTrollman.UniversalGrid;

import com.YTrollman.UniversalGrid.registry.ModNetworkHandler;
import com.refinedmods.refinedstorage.api.IRSAPI;
import com.refinedmods.refinedstorage.api.RSAPIInject;
import dev.toliner.enhancedstorage.EnhancedStorage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UniversalGrid {
    public static final Logger LOGGER = LogManager.getLogger(EnhancedStorage.MOD_ID);

    @RSAPIInject
    public static IRSAPI RSAPI;
    public static final ModNetworkHandler NETWORK_HANDLER = new ModNetworkHandler();
}
