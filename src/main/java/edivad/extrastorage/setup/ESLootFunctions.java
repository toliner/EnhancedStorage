package edivad.extrastorage.setup;

import dev.toliner.enhancedstorage.EnhancedStorage;
import edivad.extrastorage.loottable.AdvancedCrafterLootFunction;
import edivad.extrastorage.loottable.StorageBlockLootFunction;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

public class ESLootFunctions
{
    private static LootFunctionType storageBlock;
    private static LootFunctionType crafter;

    public static void register()
    {
        storageBlock = Registry.register(Registry.LOOT_FUNCTION_TYPE, new ResourceLocation(EnhancedStorage.MOD_ID, "storage_block"), new LootFunctionType(new StorageBlockLootFunction.Serializer()));
        crafter = Registry.register(Registry.LOOT_FUNCTION_TYPE, new ResourceLocation(EnhancedStorage.MOD_ID, "crafter"), new LootFunctionType(new AdvancedCrafterLootFunction.Serializer()));
    }

    public static LootFunctionType getStorageBlock()
    {
        return storageBlock;
    }

    public static LootFunctionType getCrafter()
    {
        return crafter;
    }
}
