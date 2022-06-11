plugins {
    java
    id("io.papermc.paperweight.userdev") version "1.3.6"
}

group = "io.github.kabanfriends.ecraft"
version = "1.3-SNAPSHOT"

val mcVersion = "1.18.2-R0.1-SNAPSHOT"

repositories {
    mavenCentral()

    maven("https://jitpack.io")
    maven("https://repo.dmulloy2.net/nexus/repository/public/")
    maven("https://repo.rapture.pw/repository/maven-snapshots/")
    maven("https://repo.opencollab.dev/maven-snapshots/")
    maven("https://maven.enginehub.org/repo/")
    maven("https://repo.dmulloy2.net/repository/public/")
}

dependencies {
    paperDevBundle(mcVersion)

    implementation("org.geysermc.floodgate:api:2.0-SNAPSHOT")
    implementation("com.github.koca2000:NoteBlockAPI:-SNAPSHOT")
    implementation("com.sk89q.worldguard:worldguard-bukkit:7.0.6")

    compileOnly("com.github.MilkBowl:VaultAPI:1.7") {
        exclude("org.bukkit")
    }

    //implementation("com.comphenix.protocol:ProtocolLib:4.7.0")
}

tasks {
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }
}
