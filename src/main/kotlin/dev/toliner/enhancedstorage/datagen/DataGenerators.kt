package dev.toliner.enhancedstorage.datagen

import edivad.extrastorage.datagen.*
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent

object DataGenerators {
    @SubscribeEvent
    fun gatherData(event: GatherDataEvent) {
        val generator = event.generator
        val existingFileHelper = event.existingFileHelper

        if (event.includeServer()) {
            val blockTagsProvider = Tag.BlockTags(generator, existingFileHelper)
            generator.addProvider(blockTagsProvider)
            generator.addProvider(Tag.ItemTags(generator, blockTagsProvider, existingFileHelper))
            generator.addProvider(Recipes(generator))
            generator.addProvider(Lang(generator))
            generator.addProvider(LootTableGenerator(generator))
        }
        if (event.includeClient()) {
            generator.addProvider(BlockStates(generator, existingFileHelper))
            generator.addProvider(Items(generator, existingFileHelper))
        }
    }
}
