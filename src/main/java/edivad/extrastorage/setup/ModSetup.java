package edivad.extrastorage.setup;

import dev.toliner.enhancedstorage.EnhancedStorage;
import edivad.extrastorage.blocks.CrafterTier;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EnhancedStorage.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModSetup {

    public static final ItemGroup extraStorageTab = new ItemGroup(EnhancedStorage.MOD_ID + "_tab") {

        @Override
        public ItemStack createIcon()
        {
            return new ItemStack(Registration.CRAFTER_BLOCK.get(CrafterTier.GOLD).get());
        }
    };

}
