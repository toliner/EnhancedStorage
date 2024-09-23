package com.YTrollman.UniversalGrid.registry;

import dev.toliner.enhancedstorage.EnhancedStorage;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nonnull;

public class ItemGroupUniversalGrid {
	public static final ItemGroup UNIVERSAL_GRID = (new ItemGroup(EnhancedStorage.MOD_ID) {

		@Override
		@Nonnull
		@OnlyIn(Dist.CLIENT)
		public ItemStack createIcon() {
			return new ItemStack(ModItems.CREATIVE_WIRELESS_UNIVERSAL_GRID);
		}
	});
}
