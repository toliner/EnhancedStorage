package edivad.extrastorage.items;

import com.refinedmods.refinedstorage.RSBlocks;
import com.refinedmods.refinedstorage.RSItems;
import com.refinedmods.refinedstorage.api.storage.disk.IStorageDisk;
import com.refinedmods.refinedstorage.api.storage.disk.StorageDiskSyncData;
import com.refinedmods.refinedstorage.apiimpl.API;
import com.refinedmods.refinedstorage.item.ProcessorItem;
import com.refinedmods.refinedstorage.item.blockitem.BaseBlockItem;
import com.refinedmods.refinedstorage.render.Styles;
import edivad.extrastorage.blocks.AdvancedFluidStorageBlock;
import edivad.extrastorage.items.fluid.ExpandedStorageDiskFluid;
import edivad.extrastorage.items.fluid.FluidStorageType;
import edivad.extrastorage.nodes.AdvancedFluidStorageNetworkNode;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nullable;
import java.util.List;
import java.util.UUID;

public class AdvancedFluidStorageBlockItem extends BaseBlockItem
{
    private final FluidStorageType type;

    public AdvancedFluidStorageBlockItem(AdvancedFluidStorageBlock block, Properties builder)
    {
        super(block, builder);
        this.type = block.getType();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag)
    {
        super.addInformation(stack, world, tooltip, flag);

        if (isValid(stack))
        {
            UUID id = getId(stack);

            API.instance().getStorageDiskSync().sendRequest(id);

            StorageDiskSyncData data = API.instance().getStorageDiskSync().getData(id);
            if (data != null)
            {
                if (data.getCapacity() == -1)
                    tooltip.add(new TranslationTextComponent("misc.refinedstorage.storage.stored", API.instance().getQuantityFormatter().format(data.getStored())).setStyle(Styles.GRAY));
                else
                    tooltip.add(new TranslationTextComponent("misc.refinedstorage.storage.stored_capacity", API.instance().getQuantityFormatter().format(data.getStored()), API.instance().getQuantityFormatter().format(data.getCapacity())).setStyle(Styles.GRAY));
            }

            if (flag.isAdvanced())
                tooltip.add(new StringTextComponent(id.toString()).setStyle(Styles.GRAY));
        }
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, PlayerEntity player, Hand hand)
    {
        ItemStack storageStack = player.getHeldItem(hand);

        if(!world.isRemote && player.isCrouching())
        {
            UUID diskId = null;
            IStorageDisk disk = null;

            if (isValid(storageStack))
            {
                diskId = getId(storageStack);
                disk = API.instance().getStorageDiskManager((ServerWorld) world).get(diskId);
            }

            // Newly created storages won't have a tag yet, so allow invalid disks as well.
            if (disk == null || disk.getStored() == 0)
            {
                ItemStack fluidStoragePart = new ItemStack(ExpandedStorageDiskFluid.getPartById(type));

                if (!player.inventory.addItemStackToInventory(fluidStoragePart.copy()))
                    InventoryHelper.spawnItemStack(world, player.getPosX(), player.getPosY(), player.getPosZ(), fluidStoragePart);

                ItemStack processor = new ItemStack((IItemProvider)((RegistryObject) RSItems.PROCESSORS.get(ProcessorItem.Type.BASIC)).get());
                if (!player.inventory.addItemStackToInventory(processor.copy()))
                    InventoryHelper.spawnItemStack(world, player.getPosX(), player.getPosY(), player.getPosZ(), processor);

                ItemStack bucket = new ItemStack(Items.BUCKET);
                if (!player.inventory.addItemStackToInventory(bucket.copy()))
                    InventoryHelper.spawnItemStack(world, player.getPosX(), player.getPosY(), player.getPosZ(), bucket);

                if (disk != null)
                {
                    API.instance().getStorageDiskManager((ServerWorld) world).remove(diskId);
                    API.instance().getStorageDiskManager((ServerWorld) world).markForSaving();
                }

                return new ActionResult<>(ActionResultType.SUCCESS, new ItemStack(RSBlocks.MACHINE_CASING.get()));
            }
        }
        return new ActionResult<>(ActionResultType.PASS, storageStack);
    }

    @Override
    public int getEntityLifespan(ItemStack itemStack, World world)
    {
        return Integer.MAX_VALUE;
    }

    private UUID getId(ItemStack disk)
    {
        return disk.getTag().getUniqueId(AdvancedFluidStorageNetworkNode.NBT_ID);
    }

    private boolean isValid(ItemStack disk)
    {
        return disk.hasTag() && disk.getTag().hasUniqueId(AdvancedFluidStorageNetworkNode.NBT_ID);
    }
}
