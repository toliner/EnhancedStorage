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
    testImplementation(kotlin("test"))
}

minecraft {
    mappings("official", "1.16.5")
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

            args("--mod", "enhanced_storage", "--all", "--output", "src/generated/resources/", "--existing", "src/main/resources/")

            mods {
                create("enhanced_storage") {
                    source(sourceSets.main.get())
                }
            }
        }
    }
}

kotlin {
    jvmToolchain(21)
}
