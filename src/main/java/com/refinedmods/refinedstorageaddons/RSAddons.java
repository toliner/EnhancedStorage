package com.refinedmods.refinedstorageaddons;

import com.refinedmods.refinedstorageaddons.item.group.MainItemGroup;
import com.refinedmods.refinedstorageaddons.setup.ClientSetup;
import com.refinedmods.refinedstorageaddons.setup.CommonSetup;
import net.minecraft.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public final class RSAddons {

    public static final MainItemGroup MAIN_GROUP = new MainItemGroup();

    public RSAddons() {
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ClientSetup::new);

        CommonSetup commonSetup = new CommonSetup();

        FMLJavaModLoadingContext.get().getModEventBus().addListener(commonSetup::onCommonSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addGenericListener(Item.class, commonSetup::onRegisterItems);
    }
}
