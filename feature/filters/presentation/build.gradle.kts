plugins {
    alias(libs.plugins.logfox.android.feature.compose)
}

android {
    namespace = "com.f0x1d.logfox.feature.filters.presentation"
}

dependencies {
    implementation(projects.feature.filters.api)
    implementation(projects.feature.appsPicker.api)
    implementation(projects.feature.database.api)
    implementation(projects.feature.preferences.api)
    implementation(projects.core.ui.icons)
    implementation(projects.core.ui.dialog)
    implementation(projects.core.tea.android)
    implementation(projects.core.di)
    implementation(projects.feature.datetime.api)
    implementation(projects.feature.navigation.api)
    implementation(projects.feature.logging.api)
    implementation(projects.core.ui.compose.fragment)
    implementation(projects.core.ui.compose.designSystem)
    implementation(projects.core.ui.compose.base)
    implementation(projects.strings)
    implementation(libs.bundles.androidx)
    implementation(libs.bundles.androidx.navigation)
    implementation(libs.material)
    implementation(libs.androidx.hilt.navigation.fragment)
    implementation(libs.flow.preferences)
}
