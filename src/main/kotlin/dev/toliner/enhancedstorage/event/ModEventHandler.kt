package dev.toliner.enhancedstorage.event

import com.YTrollman.UniversalGrid.apiiml.network.grid.WirelessUniversalGridGridFactory
import com.YTrollman.UniversalGrid.item.WirelessUniversalGridItem
import com.refinedmods.refinedstorage.api.network.node.INetworkNode
import com.refinedmods.refinedstorage.apiimpl.API
import com.refinedmods.refinedstorage.apiimpl.network.node.NetworkNode
import com.refinedmods.refinedstorage.tile.BaseTile
import com.refinedmods.refinedstorage.tile.data.TileDataManager
import com.refinedmods.refinedstorage.tile.data.TileDataParameter
import dev.toliner.enhancedstorage.EnhancedStorage
import edivad.extrastorage.blocks.CrafterTier
import edivad.extrastorage.compat.CarryOnIntegration
import edivad.extrastorage.compat.TOPIntegration
import edivad.extrastorage.items.fluid.FluidStorageType
import edivad.extrastorage.items.item.ItemStorageType
import edivad.extrastorage.nodes.*
import edivad.extrastorage.setup.Registration
import net.minecraft.item.Item
import net.minecraft.nbt.CompoundNBT
import net.minecraft.tileentity.TileEntityType
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.event.RegistryEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.InterModComms
import net.minecraftforge.fml.ModList
import net.minecraftforge.fml.RegistryObject
import net.minecraftforge.fml.common.Mod.EventBusSubscriber
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent

@EventBusSubscriber(modid = EnhancedStorage.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
object ModEventHandler {
    @SubscribeEvent
    fun onRegisterItems(e: RegistryEvent.Register<Item?>) {
        e.registry.register(WirelessUniversalGridItem(WirelessUniversalGridItem.Type.NORMAL))
        e.registry.register(WirelessUniversalGridItem(WirelessUniversalGridItem.Type.CREATIVE))
    }

    @SubscribeEvent
    fun setup(event: FMLCommonSetupEvent) {
        EnhancedStorage.networkHandler.register()
        API.instance().gridManager.add(WirelessUniversalGridGridFactory.ID, WirelessUniversalGridGridFactory())

        val networkNodeRegistry = API.instance().networkNodeRegistry
        for (tier in CrafterTier.entries) {
            networkNodeRegistry.add(
                ResourceLocation(EnhancedStorage.MOD_ID, tier.id)
            ) { tag: CompoundNBT, world: World, pos: BlockPos ->
                readAndReturn(tag, AdvancedCrafterNetworkNode(world, pos, tier))
            }
            Registration.CRAFTER_TILE.getValue(tier).forEachParameters { parameter: TileDataParameter<*, *>? ->
                TileDataManager.registerParameter(parameter)
            }
        }
        for (type in ItemStorageType.entries) {
            networkNodeRegistry.add(
                ResourceLocation(EnhancedStorage.MOD_ID, "block_" + type.getName())
            ) { tag: CompoundNBT, world: World, pos: BlockPos ->
                readAndReturn(tag, AdvancedStorageNetworkNode(world, pos, type))
            }
            Registration.ITEM_STORAGE_TILE.getValue(type).forEachParameters { parameter: TileDataParameter<*, *>? ->
                TileDataManager.registerParameter(parameter)
            }
        }
        for (type in FluidStorageType.entries) {
            networkNodeRegistry.add(
                ResourceLocation(EnhancedStorage.MOD_ID, "block_" + type.getName() + "_fluid")
            ) { tag: CompoundNBT, world: World, pos: BlockPos ->
                readAndReturn(tag, AdvancedFluidStorageNetworkNode(world, pos, type))
            }
            Registration.FLUID_STORAGE_TILE.getValue(type).forEachParameters { parameter: TileDataParameter<*, *>? ->
                TileDataManager.registerParameter(parameter)
            }
        }

        networkNodeRegistry.add(
            AdvancedExporterNetworkNode.ID,
        ) { tag: CompoundNBT, world: World, pos: BlockPos ->
            readAndReturn(tag, AdvancedExporterNetworkNode(world, pos))
        }
        networkNodeRegistry.add(
            AdvancedImporterNetworkNode.ID
        ) { tag: CompoundNBT, world: World, pos: BlockPos ->
            readAndReturn(tag, AdvancedImporterNetworkNode(world, pos))
        }
        Registration.ADVANCED_EXPORTER_TILE.forEachParameters { parameter: TileDataParameter<*, *>? ->
            TileDataManager.registerParameter(
                parameter
            )
        }
        Registration.ADVANCED_IMPORTER_TILE.forEachParameters { parameter: TileDataParameter<*, *>? ->
            TileDataManager.registerParameter(
                parameter
            )
        }

        //Integrations
        CarryOnIntegration.registerCarryOn()
        if (ModList.get().isLoaded("theoneprobe")) {
            InterModComms.sendTo("theoneprobe", "getTheOneProbe", ::TOPIntegration)
        }
    }

    private fun <T : BaseTile> RegistryObject<TileEntityType<T>>.forEachParameters(action: (parameter: TileDataParameter<*, *>) -> Unit) {
        get().create()!!.dataManager.parameters.forEach(action)
    }

    private fun readAndReturn(tag: CompoundNBT, node: NetworkNode): INetworkNode {
        node.read(tag)
        return node
    }
}