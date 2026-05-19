import com.diffplug.gradle.spotless.SpotlessExtension

plugins {
    alias(libs.plugins.springBoot) apply false
    alias(libs.plugins.spotless) apply false
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "checkstyle")
    apply(plugin = "com.diffplug.spotless")

    repositories {
        mavenCentral()
    }

    configure<JavaPluginExtension> {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(25))
        }
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    configure<SpotlessExtension> {
        java {
            googleJavaFormat()
            removeUnusedImports()
            trimTrailingWhitespace()
            endWithNewline()
        }
    }

    configure<org.gradle.api.plugins.quality.CheckstyleExtension> {
        toolVersion = "10.21.0"
        configFile = rootProject.file("config/checkstyle/checkstyle.xml")
    }
}
