package dev.toliner.enhancedstorage.config

import net.minecraftforge.common.ForgeConfigSpec

object UniversalGridConfig {
    lateinit var useEnergy: ForgeConfigSpec.BooleanValue
        private set
    lateinit var capacity: ForgeConfigSpec.IntValue
        private set
    lateinit var openUsage: ForgeConfigSpec.IntValue
        private set
    lateinit var craftUsage: ForgeConfigSpec.IntValue
        private set
    lateinit var clearUsage: ForgeConfigSpec.IntValue
        private set

    fun initCommon(builder: ForgeConfigSpec.Builder) {
        builder.run {
            push("universal_grid")
            comment("Universal Grid settings")

            useEnergy = comment("Whether the Universal Grid uses energy")
                .translation("config.enhanced_storage.universal_grid.use_energy")
                .define("use_energy", true)

            capacity = comment("The capacity of the Universal Grid")
                .translation("config.enhanced_storage.universal_grid.capacity")
                .defineInRange("capacity", 10000, 0, Int.MAX_VALUE)

            openUsage = comment("The energy usage of opening the Universal Grid")
                .translation("config.enhanced_storage.universal_grid.open_usage")
                .defineInRange("open_usage", 50, 0, Int.MAX_VALUE)

            craftUsage = comment("The energy usage of crafting with the Universal Grid")
                .translation("config.enhanced_storage.universal_grid.craft_usage")
                .defineInRange("craft_usage", 2, 0, Int.MAX_VALUE)

            clearUsage = comment("The energy usage of clearing the Universal Grid")
                .translation("config.enhanced_storage.universal_grid.clear_usage")
                .defineInRange("clear_usage", 20, 0, Int.MAX_VALUE)
            pop()
        }

    }
}