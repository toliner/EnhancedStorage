package dev.toliner.enhancedstorage.config

import com.electronwill.nightconfig.core.file.CommentedFileConfig
import dev.toliner.enhancedstorage.EnhancedStorage
import net.minecraftforge.common.ForgeConfigSpec
import java.nio.file.Path

object Config {
    val commonConfig: ForgeConfigSpec

    init {
        val builder = ForgeConfigSpec.Builder()
        UniversalGridConfig.initCommon(builder)
        commonConfig = builder.build()
    }

    fun load(config: ForgeConfigSpec, path: String) {
        EnhancedStorage.LOGGER.info("Loading config: $path")
        val file = CommentedFileConfig.builder(Path.of(path))
            .autosave()
            .autoreload()
            .build()
        config.setConfig(file)
    }
}