package dev.toliner.enhancedstorage.event

import com.YTrollman.UniversalGrid.registry.ModItems
import com.YTrollman.UniversalGrid.registry.ModKeyBindings
import com.refinedmods.refinedstorage.screen.KeyInputListener
import dev.toliner.enhancedstorage.EnhancedStorage
import net.minecraft.client.Minecraft
import net.minecraftforge.client.event.InputEvent.KeyInputEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod.EventBusSubscriber

@EventBusSubscriber(modid = EnhancedStorage.MOD_ID, bus = EventBusSubscriber.Bus.FORGE)
object ForgeEventHandler {
    @SubscribeEvent
    fun onKeyInput(e: KeyInputEvent?) {
        if (Minecraft.getInstance().player != null) {
            if (ModKeyBindings.OPEN_WIRELESS_UNIVERSAL_GRID.isKeyDown) {
                KeyInputListener.findAndOpen(
                    ModItems.WIRELESS_UNIVERSAL_GRID,
                    ModItems.CREATIVE_WIRELESS_UNIVERSAL_GRID
                )
            }
        }
    }
}