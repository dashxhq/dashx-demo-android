object Versions {

    const val dashXAppVersion = "1.0.0"
    const val dashXAppVersionCode = 100000

    // Build tools and languages
    const val androidPlugin = "7.0.3"
    const val kotlin = "1.5.31"

    const val coreLibraryDesugar = "1.1.5"

    // Kotlinx
    const val coroutinesVersion = "1.5.1"
    const val kotlinSerializationJson = "1.3.0"

    // Network
    const val ktor = "1.6.2"

    // Google/AndroidX
    const val androidXCore = "1.7.0"
    const val androidXBiometric = "1.1.0"
    const val legacySupport = "1.0.0"
    const val appCompat = "1.3.1"
    const val navigationSafeArgs = "2.3.5"
    const val recyclerView = "1.2.1"
    const val constraintLayout = "2.1.0"
    const val material = "1.4.0"
    const val lifecycle = "2.4.0-rc01"


    // Testing
    const val junit = "4.13.2"

    const val androidXJunit = "1.1.3"
    const val androidXEspressoCore = "3.4.0"

}

object AppConfig {

    // SDK info
    const val minSdk = 24
    const val targetSdk = 31
    const val compileSdk = 31
}

object Dependencies {
    const val androidGradlePlugin = "com.android.tools.build:gradle:${Versions.androidPlugin}"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val kotlinSerializationPlugin =
        "org.jetbrains.kotlin:kotlin-serialization:${Versions.kotlin}"
    const val navigationSafeArgsPlugin =
        "androidx.navigation:navigation-safe-args-gradle-plugin:${Versions.navigationSafeArgs}"

    const val coreLibraryDesugar =
        "com.android.tools:desugar_jdk_libs:${Versions.coreLibraryDesugar}"

    // Kotlinx
    const val coroutinesCore =
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutinesVersion}"
    const val kotlinSerializationJson =
        "org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.kotlinSerializationJson}"

    // Google/AndroidX
    const val androidXCore = "androidx.core:core-ktx:${Versions.androidXCore}"
    const val legacySupport = "androidx.legacy:legacy-support-v4:${Versions.legacySupport}"
    const val appCompat = "androidx.appcompat:appcompat:${Versions.appCompat}"
    const val recyclerView = "androidx.recyclerview:recyclerview:${Versions.recyclerView}"
    const val material = "com.google.android.material:material:${Versions.material}"
    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val lifecycleCommon = "androidx.lifecycle:lifecycle-common-java8:${Versions.lifecycle}"
    const val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
    const val lifecycleViewModel =
        "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    const val lifecycleLivedata = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.lifecycle}"
    const val lifecycleProcess = "androidx.lifecycle:lifecycle-process:${Versions.lifecycle}"

    // Tests
    const val junit = "junit:junit:${Versions.junit}"

    // Network
    const val ktor = "io.ktor:ktor-client-core:${Versions.ktor}"
    const val ktorAuth = "io.ktor:ktor-client-auth:${Versions.ktor}"
    const val ktorHttp = "io.ktor:ktor-client-okhttp:${Versions.ktor}"
    const val ktorSerialization = "io.ktor:ktor-client-serialization:${Versions.ktor}"
    const val ktorLogging = "io.ktor:ktor-client-logging:${Versions.ktor}"

    // Android Test
    const val androidXJunit = "androidx.test.ext:junit:${Versions.androidXJunit}"
    const val androidXEspressoCore =
        "androidx.test.espresso:espresso-core:${Versions.androidXEspressoCore}"

}