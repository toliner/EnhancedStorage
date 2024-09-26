package com.YTrollman.UniversalGrid.item;

import com.YTrollman.UniversalGrid.apiiml.network.item.WirelessUniversalGridNetworkItem;
import com.refinedmods.refinedstorage.api.network.INetwork;
import com.refinedmods.refinedstorage.api.network.item.INetworkItem;
import com.refinedmods.refinedstorage.api.network.item.INetworkItemManager;
import com.refinedmods.refinedstorage.inventory.player.PlayerSlot;
import com.refinedmods.refinedstorage.item.NetworkItem;
import com.refinedmods.refinedstorage.util.NetworkUtils;
import dev.toliner.enhancedstorage.EnhancedStorage;
import dev.toliner.enhancedstorage.config.UniversalGridConfig;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ActionResultType;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class WirelessUniversalGridItem extends NetworkItem {
    public enum Type {
        NORMAL,
        CREATIVE
    }

    private final Type type;

    public WirelessUniversalGridItem(Type type) {
        super(new Properties().group(EnhancedStorage.getEnhancedStorageGroup()).maxStackSize(1),
                type == Type.CREATIVE,
                () -> UniversalGridConfig.INSTANCE.getCapacity().get());

        this.type = type;

        this.setRegistryName(EnhancedStorage.MOD_ID, (type == Type.CREATIVE ? "creative_" : "") + "wireless_universal_grid");
    }

    @Override
    public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected) {
        if (!world.isRemote() && entity instanceof PlayerEntity) {
            CompoundNBT tag = stack.getTag();
            if (tag == null) {
                tag = new CompoundNBT();
            }

            if(!tag.contains("gridType")) {
                tag.putInt("gridType", 0);
                stack.setTag(tag);
            }
        }
    }


    @Override
    public ActionResultType onItemUse(ItemUseContext ctx) {
        ItemStack stack = ctx.getPlayer().getHeldItem(ctx.getHand());
        INetwork network = NetworkUtils.getNetworkFromNode(NetworkUtils.getNodeFromTile(ctx.getWorld().getTileEntity(ctx.getPos())));
        if (network != null) {
            CompoundNBT tag = stack.getTag();
            if (tag == null) {
                tag = new CompoundNBT();
            }

            tag.putInt("NodeX", network.getPosition().getX());
            tag.putInt("NodeY", network.getPosition().getY());
            tag.putInt("NodeZ", network.getPosition().getZ());
            tag.putString("Dimension", ctx.getWorld().getDimensionKey().getLocation().toString());
            stack.setTag(tag);
            return ActionResultType.SUCCESS;
        } else {
            return ActionResultType.PASS;
        }
    }

    public Type getType() {
        return type;
    }

    @Override
    @Nonnull
    public INetworkItem provide(INetworkItemManager handler, PlayerEntity player, ItemStack stack, PlayerSlot slot) {
        return new WirelessUniversalGridNetworkItem(handler, player, stack, slot);
    }
}
