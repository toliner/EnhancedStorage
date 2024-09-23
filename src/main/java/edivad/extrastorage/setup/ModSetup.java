package edivad.extrastorage.setup;

import edivad.extrastorage.Main;
import edivad.extrastorage.blocks.CrafterTier;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Main.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModSetup {

    public static final ItemGroup extraStorageTab = new ItemGroup(Main.MODID + "_tab") {

        @Override
        public ItemStack createIcon()
        {
            return new ItemStack(Registration.CRAFTER_BLOCK.get(CrafterTier.GOLD).get());
        }
    };

}
