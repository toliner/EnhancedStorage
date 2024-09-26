package com.refinedmods.refinedstorageaddons;

import com.refinedmods.refinedstorageaddons.item.group.MainItemGroup;
import com.refinedmods.refinedstorageaddons.setup.CommonSetup;
import net.minecraft.item.Item;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public final class RSAddons {

    public static final MainItemGroup MAIN_GROUP = new MainItemGroup();

    public RSAddons() {

        CommonSetup commonSetup = new CommonSetup();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(commonSetup::onCommonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Item.class, commonSetup::onRegisterItems);
    }
}
