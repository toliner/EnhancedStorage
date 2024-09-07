package dev.toliner.enhancedstorage

import edivad.extrastorage.setup.ClientSetup
import edivad.extrastorage.setup.ESLootFunctions
import edivad.extrastorage.setup.ModSetup
import edivad.extrastorage.setup.Registration
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.fml.DistExecutor
import net.minecraftforge.fml.DistExecutor.SafeRunnable
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import org.apache.logging.log4j.LogManager
import thedarkcolour.kotlinforforge.KotlinModLoadingContext

@Mod(EnhancedStorage.MOD_ID)
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
object EnhancedStorage {
    const val MOD_ID = "enhanced_storage"
    val LOGGER = LogManager.getLogger()

    init {
        DistExecutor.safeRunWhenOn(
            Dist.CLIENT,
        ) { SafeRunnable(::ClientSetup) }
        Registration.init()
        ESLootFunctions.register()

        // Register the setup method for modloading
        KotlinModLoadingContext.get().getKEventBus().addListener { event: FMLCommonSetupEvent? ->
            ModSetup.init(
                event
            )
        }
    }
}
