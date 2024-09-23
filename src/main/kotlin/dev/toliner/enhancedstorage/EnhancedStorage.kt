package dev.toliner.enhancedstorage

import com.YTrollman.UniversalGrid.UniversalGrid
import com.YTrollman.UniversalGrid.apiiml.network.grid.WirelessUniversalGridGridFactory
import com.YTrollman.UniversalGrid.config.Config
import com.YTrollman.UniversalGrid.item.WirelessUniversalGridItem
import com.YTrollman.UniversalGrid.registry.ModItems
import com.YTrollman.UniversalGrid.registry.ModKeyBindings
import com.refinedmods.refinedstorage.screen.KeyInputListener
import dev.toliner.enhancedstorage.client.ClientSetup
import dev.toliner.enhancedstorage.datagen.DataGenerators
import edivad.extrastorage.setup.ESLootFunctions
import edivad.extrastorage.setup.ModSetup
import edivad.extrastorage.setup.Registration
import net.minecraft.client.Minecraft
import net.minecraft.item.Item
import net.minecraft.util.ResourceLocation
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.InputEvent.KeyInputEvent
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.DistExecutor
import net.minecraftforge.fml.ModLoadingContext
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.fml.config.ModConfig
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent
import net.minecraftforge.fml.loading.FMLPaths
import org.apache.logging.log4j.LogManager
import thedarkcolour.kotlinforforge.KotlinModLoadingContext
import thedarkcolour.kotlinforforge.forge.FORGE_BUS

@Mod(EnhancedStorage.MOD_ID)
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
object EnhancedStorage {
    const val MOD_ID = "enhanced_storage"
    val LOGGER = LogManager.getLogger()

    init {
        DistExecutor.safeRunWhenOn(
            Dist.CLIENT,
        ) { DistExecutor.SafeRunnable(::ClientSetup) }

        Registration.init()
        ESLootFunctions.register()

        // Register the setup method for modloading
        KotlinModLoadingContext.get().getKEventBus().addListener { event: FMLCommonSetupEvent ->
            ModSetup.init(event)
            setup(event)
        }

        KotlinModLoadingContext.get().getKEventBus().addListener { event: GatherDataEvent ->
            DataGenerators.gatherData(event)
        }

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.common_config)
        Config.loadConfig(
            Config.common_config,
            FMLPaths.CONFIGDIR.get().resolve("universalgrid-common.toml").toString()
        )

        FORGE_BUS.addListener(this::onKeyInput)
    }

    private fun setup(event: FMLCommonSetupEvent) {
        UniversalGrid.NETWORK_HANDLER.register()
        UniversalGrid.RSAPI.gridManager.add(WirelessUniversalGridGridFactory.ID, WirelessUniversalGridGridFactory())
    }

    @SubscribeEvent
    fun onRegisterItems(e: RegistryEvent.Register<Item?>) {
        e.registry.register(WirelessUniversalGridItem(WirelessUniversalGridItem.Type.NORMAL))
        e.registry.register(WirelessUniversalGridItem(WirelessUniversalGridItem.Type.CREATIVE))
    }

    private fun onKeyInput(e: KeyInputEvent?) {
        if (Minecraft.getInstance().player != null) {
            if (ModKeyBindings.OPEN_WIRELESS_UNIVERSAL_GRID.isKeyDown) {
                KeyInputListener.findAndOpen(
                    ModItems.WIRELESS_UNIVERSAL_GRID,
                    ModItems.CREATIVE_WIRELESS_UNIVERSAL_GRID
                )
            }
        }
    }

    @JvmStatic
    fun ResourceLocation(name: String): ResourceLocation {
        return ResourceLocation(MOD_ID, name)
    }
}
