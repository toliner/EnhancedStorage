plugins {
    kotlin("jvm") version "2.0.10"
    id("net.minecraftforge.gradle") version "[6.0,6.2)"
    `maven-publish`
}

group = "dev.toliner"
version = "1.0-SNAPSHOT"

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

            args("--mod", "enhanced_storage", "--all", "--output", "../src/generated/resources/", "--existing", "../src/main/resources/")

            mods {
                create("enhanced_storage") {
                    source(sourceSets.main.get())
                }
            }
        }
    }
}

sourceSets {
    main.get().resources.srcDir("src/generated/resources")
}

kotlin {
    jvmToolchain(21)
}
