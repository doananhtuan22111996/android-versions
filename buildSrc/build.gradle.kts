plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation("com.android.tools.build:gradle:8.4.2")
    implementation("com.android.tools.build:gradle-api:8.4.2")
    implementation("com.squareup:kotlinpoet:1.12.0")
}
