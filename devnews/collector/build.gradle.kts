plugins {
    alias(libs.plugins.springBoot)
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    implementation(platform(libs.spring.boot.bom))
    implementation(project(":devnews:domain"))
    implementation(libs.spring.boot.starter.quartz)
    implementation(libs.rome)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    runtimeOnly(libs.postgresql)
    testImplementation(libs.spring.boot.starter.test)
    testRuntimeOnly(libs.junit.platform.launcher)
}
