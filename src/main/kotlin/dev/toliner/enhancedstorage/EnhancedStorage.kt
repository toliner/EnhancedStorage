package dev.toliner.enhancedstorage

import com.YTrollman.UniversalGrid.registry.ModNetworkHandler
import dev.toliner.enhancedstorage.config.Config
import edivad.extrastorage.blocks.CrafterTier
import edivad.extrastorage.setup.ESLootFunctions
import edivad.extrastorage.setup.Registration
import net.minecraft.item.ItemGroup
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.fml.config.ModConfig
import net.minecraftforge.fml.loading.FMLPaths
import org.apache.logging.log4j.LogManager

@Mod(EnhancedStorage.MOD_ID)
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
object EnhancedStorage {
    const val MOD_ID = "enhanced_storage"
    const val MOD_NAME = "Enhanced Storage"
    @JvmStatic
    val LOGGER = LogManager.getLogger()
    val networkHandler: ModNetworkHandler = ModNetworkHandler()

    @JvmStatic
    val enhancedStorageGroup: ItemGroup = object : ItemGroup(MOD_ID + "_tab") {
        override fun createIcon(): ItemStack {
            return ItemStack(Registration.CRAFTER_BLOCK[CrafterTier.GOLD]!!.get())
        }
    }

    init {
        Registration.init()
        ESLootFunctions.register()

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.commonConfig)
        Config.load(
            Config.commonConfig,
            FMLPaths.CONFIGDIR.get().resolve("$MOD_ID-common.toml").toString()
        )
    }

    @JvmStatic
    fun ResourceLocation(name: String): ResourceLocation {
        return ResourceLocation(MOD_ID, name)
    }
}
