package dev.toliner.enhancedstorage

import com.YTrollman.UniversalGrid.config.Config
import edivad.extrastorage.setup.ESLootFunctions
import edivad.extrastorage.setup.Registration
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
    val LOGGER = LogManager.getLogger()

    init {
        Registration.init()
        ESLootFunctions.register()

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.common_config)
        Config.loadConfig(
            Config.common_config,
            FMLPaths.CONFIGDIR.get().resolve("universalgrid-common.toml").toString()
        )
    }

    @JvmStatic
    fun ResourceLocation(name: String): ResourceLocation {
        return ResourceLocation(MOD_ID, name)
    }
}
