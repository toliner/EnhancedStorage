package dev.toliner.enhancedstorage.client

import com.YTrollman.UniversalGrid.registry.ModItems
import com.YTrollman.UniversalGrid.registry.ModKeyBindings
import com.refinedmods.refinedstorage.apiimpl.API
import com.refinedmods.refinedstorage.item.property.NetworkItemPropertyGetter
import com.refinedmods.refinedstorage.render.BakedModelOverrideRegistry
import com.refinedmods.refinedstorage.render.model.FullbrightBakedModel
import com.refinedmods.refinedstorageaddons.RSAddonsItems
import dev.toliner.enhancedstorage.EnhancedStorage
import edivad.extrastorage.blocks.CrafterTier
import edivad.extrastorage.client.screen.*
import edivad.extrastorage.container.AdvancedCrafterContainer
import edivad.extrastorage.items.fluid.FluidStorageType
import edivad.extrastorage.items.item.ItemStorageType
import edivad.extrastorage.setup.Registration
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.ScreenManager
import net.minecraft.client.renderer.RenderType
import net.minecraft.client.renderer.RenderTypeLookup
import net.minecraft.client.renderer.model.IBakedModel
import net.minecraft.item.ItemModelsProperties
import net.minecraft.item.ItemStack
import net.minecraft.util.ResourceLocation
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.client.event.ModelBakeEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.ModList
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent

@EventBusSubscriber(Dist.CLIENT, modid = EnhancedStorage.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
object ClientSetup {

    private val bakedModelOverrideRegistry = BakedModelOverrideRegistry()

    @SubscribeEvent
    fun initClient(event: FMLClientSetupEvent) {

        // register universal grid item property
        ItemModelsProperties.registerProperty(
            ModItems.WIRELESS_UNIVERSAL_GRID,
            ResourceLocation("connected"),
            NetworkItemPropertyGetter()
        )
        ItemModelsProperties.registerProperty(
            ModItems.CREATIVE_WIRELESS_UNIVERSAL_GRID,
            ResourceLocation("connected"),
            NetworkItemPropertyGetter()
        )
        ItemModelsProperties.registerProperty(
            RSAddonsItems.WIRELESS_CRAFTING_GRID,
            ResourceLocation("connected"),
            NetworkItemPropertyGetter()
        )
        ItemModelsProperties.registerProperty(
            RSAddonsItems.CREATIVE_WIRELESS_CRAFTING_GRID,
            ResourceLocation("connected"),
            NetworkItemPropertyGetter()
        )

        ClientRegistry.registerKeyBinding(ModKeyBindings.OPEN_WIRELESS_UNIVERSAL_GRID)

        // register renderers for crafters
        val crafterScreen = if (ModList.get().isLoaded("quark")) {
            ::AdvancedCrafterScreenQuark
        } else ::AdvancedCrafterScreen
        for (tier in CrafterTier.values()) {
            ScreenManager.registerFactory(
                Registration.CRAFTER_CONTAINER.getValue(tier).get(),
                crafterScreen
            )
            RenderTypeLookup.setRenderLayer(
                Registration.CRAFTER_BLOCK.getValue(tier).get(),
                RenderType.getCutout()
            )
            val parent = "blocks/crafter/${tier.name.lowercase()}/cutouts/"
            bakedModelOverrideRegistry.add(
                Registration.CRAFTER_BLOCK[tier]!!.id
            ) { base: IBakedModel, _ ->
                FullbrightBakedModel(
                    base,
                    true,
                    EnhancedStorage.ResourceLocation(parent + "side_connected"),
                    EnhancedStorage.ResourceLocation(parent + "top_connected")
                )
            }
        }

        // register renderers for advanced exporter and importer
        ScreenManager.registerFactory(Registration.ADVANCED_EXPORTER_CONTAINER.get(), ::AdvancedExporterScreen)
        ScreenManager.registerFactory(Registration.ADVANCED_IMPORTER_CONTAINER.get(), ::AdvancedImporterScreen)

        RenderTypeLookup.setRenderLayer(Registration.ADVANCED_EXPORTER.get(), RenderType.getCutout())
        RenderTypeLookup.setRenderLayer(Registration.ADVANCED_IMPORTER.get(), RenderType.getCutout())

        // register renderers for storage blocks
        for (type in ItemStorageType.values()) {
            ScreenManager.registerFactory(
                Registration.ITEM_STORAGE_CONTAINER.getValue(type).get(),
                ::AdvancedStorageBlockScreen
            )
        }
        for (type in FluidStorageType.values()) {
            ScreenManager.registerFactory(
                Registration.FLUID_STORAGE_CONTAINER.getValue(type).get(),
                ::AdvancedFluidStorageBlockScreen
            )
        }

        // Handling pattern rendering on crafters
        API.instance().addPatternRenderHandler { pattern: ItemStack ->
            val container = Minecraft.getInstance().player!!.openContainer
            if (container is AdvancedCrafterContainer) {
                val slots = container.tile!!.tier.slots
                for (i in 0 until slots) {
                    if (container.getSlot(i).stack == pattern) return@addPatternRenderHandler true
                }
            }
            false
        }
    }

    @SubscribeEvent
    fun onModelBake(event: ModelBakeEvent) {
        FullbrightBakedModel.invalidateCache()
        val modelRegistry = event.modelRegistry
        for (id in modelRegistry.keys) {
            val factory = bakedModelOverrideRegistry[ResourceLocation(id.namespace, id.path)]

            if (factory != null) {
                modelRegistry[id] = factory.create(modelRegistry[id], modelRegistry)
            }
        }
    }
}