package com.YTrollman.UniversalGrid.registry;

import com.YTrollman.UniversalGrid.apiiml.network.grid.WirelessUniversalGridSettingsUpdateMessage;
import dev.toliner.enhancedstorage.EnhancedStorage;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class ModNetworkHandler {
    private final String protocolVersion = Integer.toString(1);
    private final SimpleChannel handler = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(EnhancedStorage.MOD_ID, "main_channel"))
            .clientAcceptedVersions(protocolVersion::equals)
            .serverAcceptedVersions(protocolVersion::equals)
            .networkProtocolVersion(() -> protocolVersion)
            .simpleChannel();

    public void register() {
        int id = 0;
        this.handler.registerMessage(id++, WirelessUniversalGridSettingsUpdateMessage.class, WirelessUniversalGridSettingsUpdateMessage::encode, WirelessUniversalGridSettingsUpdateMessage::decode, WirelessUniversalGridSettingsUpdateMessage::handle);
    }

    public void sendToServer(Object message) {
        this.handler.sendToServer(message);
    }
}