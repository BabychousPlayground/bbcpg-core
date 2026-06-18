import java.nio.file.Paths

plugins {
    kotlin("jvm") version "2.3.21"
    java
    id("com.gradleup.shadow") version "9.4.2"
}

group = "fr.tartur.bbcpg"
version = "v1.0"

repositories {
    mavenCentral()
    maven {
        name = "papermc"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven("https://repo.xenondevs.xyz/releases")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:26.1.2.build.+")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.3.21")
    implementation("de.exlll:configlib-yaml:4.8.1")
    implementation("xyz.xenondevs.invui:invui-kotlin:2.1.0")
    implementation("com.zaxxer:HikariCP:7.0.2")
}

kotlin {
    jvmToolchain(25)
}

tasks.test {
    useJUnitPlatform()
}

tasks.register<Delete>("deleteOldJar") {
    val shadowJar = tasks.shadowJar.get()
    val serverArchive = Paths.get(properties["minecraft.server"].toString())
        .resolve("plugins", shadowJar.archiveFileName.get())
    delete(serverArchive)
}

tasks.register<Copy>("copyNewJar") {
    val shadowJar = tasks.shadowJar.get()
    val serverArchive = Paths.get(properties["minecraft.server"].toString())
        .resolve("plugins", shadowJar.archiveFileName.get())
    copy {
        from(shadowJar.archiveFile.get().asFile.toPath().toString())
        into(serverArchive)
    }
}

tasks.register<Exec>("runServer") {
    dependsOn("shadowJar", "deleteOldJar", "copyNewJar")

    val server = properties["minecraft.server"].toString()
    val serverStartScript = properties["minecraft.start"].toString()

    workingDir(server)
    executable(serverStartScript)

    println("Set working dir '${workingDir.toPath()}' with executable '$executable'")
}