plugins {
    `java-library`
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    api(platform(libs.spring.boot.bom))
    api(libs.spring.boot.starter.data.jpa)
    runtimeOnly(libs.postgresql)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    testImplementation(libs.spring.boot.starter.test)
    testRuntimeOnly(libs.junit.platform.launcher)
}
