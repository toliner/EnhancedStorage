package com.YTrollman.UniversalGrid.init;

import com.YTrollman.UniversalGrid.registry.ModItems;
import com.refinedmods.refinedstorage.item.property.NetworkItemPropertyGetter;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import thedarkcolour.kotlinforforge.KotlinModLoadingContext;

public class ClientEventHandler {

    public ClientEventHandler() {
        KotlinModLoadingContext.Companion.get().getKEventBus().addListener(this::init);
    }

    public void init(FMLClientSetupEvent event) {
        ItemModelsProperties.registerProperty(ModItems.WIRELESS_UNIVERSAL_GRID, new ResourceLocation("connected"), new NetworkItemPropertyGetter());
        ItemModelsProperties.registerProperty(ModItems.CREATIVE_WIRELESS_UNIVERSAL_GRID, new ResourceLocation("connected"), new NetworkItemPropertyGetter());
    }
}