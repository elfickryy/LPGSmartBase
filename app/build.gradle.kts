plugins { alias(libs.plugins.android.application); alias(libs.plugins.kotlin.android); alias(libs.plugins.kotlin.kapt) }

android {
    namespace = "id.lpgsmartbase"
    compileSdk = 35
    buildToolsVersion = "36.1.0"
    defaultConfig { applicationId = "id.lpgsmartbase"; minSdk = 24; targetSdk = 35; versionCode = 1; versionName = "1.0" }
    buildFeatures { viewBinding = true }
    compileOptions { sourceCompatibility = JavaVersion.VERSION_17; targetCompatibility = JavaVersion.VERSION_17 }
    kotlinOptions { jvmTarget = "17" }
}
kapt { arguments { arg("room.schemaLocation", "$projectDir/schemas") } }
dependencies {
    implementation(libs.core.ktx); implementation(libs.appcompat); implementation(libs.material); implementation(libs.constraintlayout)
    implementation(libs.lifecycle.runtime); implementation(libs.lifecycle.viewmodel); implementation(libs.navigation.fragment); implementation(libs.navigation.ui)
    implementation(libs.room.runtime); implementation(libs.room.ktx); kapt(libs.room.compiler)
}
