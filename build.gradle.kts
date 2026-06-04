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