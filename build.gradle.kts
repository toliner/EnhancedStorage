import org.spongepowered.asm.gradle.plugins.MixinExtension
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

buildscript {
    repositories {
        maven(url = "https://repo.spongepowered.org/maven")
    }
    dependencies {
        classpath("org.spongepowered:mixingradle:0.7-SNAPSHOT")
    }
}

apply(plugin = "org.spongepowered.mixin")

plugins {
    kotlin("jvm") version "2.0.10"
    id("net.minecraftforge.gradle") version "[6.0,6.2)"
    `maven-publish`
}

group = "dev.toliner"
version = "1.0.0"

repositories {
    mavenCentral()
    maven(url = "https://cursemaven.com") {
        name = "CurseForge"
    }
    maven(url = "https://thedarkcolour.github.io/KotlinForForge/") {
        name = "KotlinForForge"
    }
    maven(url = "https://maven.blamejared.com/") {
        name = "vazkii"
    }
    maven(url = "https://maven.tterrag.com/") {
        name = "TOP"
    }
    maven(url = "https://dvs1.progwml6.com/files/maven") {
        name = "JEI"
    }
}

dependencies {
    "minecraft"("net.minecraftforge:forge:1.16.5-36.2.42")
    implementation(fg.deobf("curse.maven:refined-storage-243076:3807951"))
    implementation("thedarkcolour:kotlinforforge:1.17.0")
    compileOnly(fg.deobf("mcjty.theoneprobe:TheOneProbe-1.16:1.16-3.1.4-22:api"))
    runtimeOnly(fg.deobf("mcjty.theoneprobe:TheOneProbe-1.16:1.16-3.1.4-22"))
    runtimeOnly(fg.deobf("mezz.jei:jei-1.16.5:7.7.1.126"))
    compileOnly(fg.deobf("vazkii.autoreglib:AutoRegLib:1.6-49.90"))
    compileOnly(fg.deobf("vazkii.quark:Quark:r2.4-321.2012"))
    testImplementation(kotlin("test"))
}

minecraft {
    mappings("snapshot", "20210309-1.16.5")
    accessTransformer(file("src/main/resources/META-INF/accesstransformer.cfg"))

    runs {
        create("client") {
            workingDirectory = "run"
            property("forge.logging.markers", "SCAN,REGISTRIES,REGISTRYDUMP")
            property("forge.logging.console.level", "debug")

            arg("-mixin.config=enhancedstorage.mixins.json")
            mods {
                create("enhanced_storage") {
                    source(sourceSets.main.get())
                }
            }
        }
        create("data") {
            workingDirectory = "run"
            property("forge.logging.markers", "SCAN,REGISTRIES,REGISTRYDUMP")
            property("forge.logging.console.level", "debug")

            args("--mod", "enhanced_storage", "--all", "--output", "../src/generated/resources/", "--existing", "../src/main/resources/", "-mixin.config=enhancedstorage.mixins.json")

            mods {
                create("enhanced_storage") {
                    source(sourceSets.main.get())
                }
            }
        }
    }
}

configure<MixinExtension> {
    add(sourceSets.main.get(), "enhancedstorage.refmap.json")
}

sourceSets {
    main.get().resources.srcDir("src/generated/resources")
}

kotlin {
    jvmToolchain(21)
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
        vendor.set(JvmVendorSpec.JETBRAINS)
    }
}

tasks.jar {
    manifest {
        attributes(
            "Specification-Title" to "enhancedstorage",
            "Specification-Vendor" to "toliner",
            "Specification-Version" to "1",
            "Implementation-Title" to project.name,
            "Implementation-Version" to project.version,
            "Implementation-Vendor" to "toliner",
            "Implementation-Timestamp" to DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneId.systemDefault()).format(Instant.now()),
        )
    }
}
