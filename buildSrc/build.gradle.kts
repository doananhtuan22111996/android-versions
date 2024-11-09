plugins {
    `kotlin-dsl`
}

repositories {
    google()
    mavenCentral()
}

dependencies {
    implementation("com.android.tools.build:gradle:8.7.2")
    implementation("com.android.tools.build:gradle-api:8.7.2")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.0.20")
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:2.0.20")
    implementation("com.squareup:kotlinpoet:2.0.0")
}
