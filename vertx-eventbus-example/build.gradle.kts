group = "ru.my.games"
version = "1.0-SNAPSHOT"

plugins {
    id("java")
}

repositories {
    mavenCentral()
}

subprojects {
    apply {
        plugin("java")
    }
    repositories {
        mavenCentral()
    }

}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}