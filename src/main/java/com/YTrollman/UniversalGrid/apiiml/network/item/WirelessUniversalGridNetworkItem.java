package com.YTrollman.UniversalGrid.apiiml.network.item;

import com.YTrollman.UniversalGrid.apiiml.network.grid.WirelessUniversalGridGridFactory;
import com.YTrollman.UniversalGrid.item.WirelessUniversalGridItem;
import com.refinedmods.refinedstorage.api.network.INetwork;
import com.refinedmods.refinedstorage.api.network.item.INetworkItem;
import com.refinedmods.refinedstorage.api.network.item.INetworkItemManager;
import com.refinedmods.refinedstorage.api.network.security.Permission;
import com.refinedmods.refinedstorage.apiimpl.API;
import com.refinedmods.refinedstorage.inventory.player.PlayerSlot;
import com.refinedmods.refinedstorage.util.WorldUtils;
import dev.toliner.enhancedstorage.config.UniversalGridConfig;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

public class WirelessUniversalGridNetworkItem implements INetworkItem {
    private INetworkItemManager handler;
    private PlayerEntity player;
    private ItemStack stack;
    private PlayerSlot slot;

    public WirelessUniversalGridNetworkItem(INetworkItemManager handler, PlayerEntity player, ItemStack stack, PlayerSlot slot) {
        this.handler = handler;
        this.player = player;
        this.stack = stack;
        this.slot = slot;
    }

    @Override
    public PlayerEntity getPlayer() {
        return player;
    }

    @Override
    public boolean onOpen(INetwork network) {
        IEnergyStorage energy = stack.getCapability(CapabilityEnergy.ENERGY, null).orElse(null);

        if (UniversalGridConfig.INSTANCE.getUseEnergy().get() &&
                ((WirelessUniversalGridItem) stack.getItem()).getType() != WirelessUniversalGridItem.Type.CREATIVE &&
                energy != null &&
                energy.getEnergyStored() <= UniversalGridConfig.INSTANCE.getOpenUsage().get()) {
            sendOutOfEnergyMessage();

            return false;
        }

        if (!network.getSecurityManager().hasPermission(Permission.MODIFY, player)) {
            WorldUtils.sendNoPermissionMessage(player);

            return false;
        }

        API.instance().getGridManager().openGrid(WirelessUniversalGridGridFactory.ID, (ServerPlayerEntity) player, stack, slot);

        drainEnergy(UniversalGridConfig.INSTANCE.getOpenUsage().get());

        return true;
    }

    @Override
    public void drainEnergy(int energy) {
        if (UniversalGridConfig.INSTANCE.getUseEnergy().get() && ((WirelessUniversalGridItem) stack.getItem()).getType() != WirelessUniversalGridItem.Type.CREATIVE) {
            stack.getCapability(CapabilityEnergy.ENERGY).ifPresent(energyStorage -> {
                energyStorage.extractEnergy(energy, false);

                if (energyStorage.getEnergyStored() <= 0) {
                    handler.close(player);

                    player.closeScreen();

                    sendOutOfEnergyMessage();
                }
            });
        }
    }

    private void sendOutOfEnergyMessage() {
        player.sendMessage(new TranslationTextComponent("misc.refinedstorage.network_item.out_of_energy", new TranslationTextComponent(stack.getItem().getTranslationKey())), player.getUniqueID());
    }
}