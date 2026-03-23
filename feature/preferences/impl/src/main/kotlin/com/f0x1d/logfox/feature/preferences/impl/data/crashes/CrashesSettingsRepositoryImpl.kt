package com.f0x1d.logfox.feature.preferences.impl.data.crashes

import com.f0x1d.logfox.core.preferences.api.PreferenceStateFlow
import com.f0x1d.logfox.core.preferences.impl.asPreferenceStateFlow
import com.f0x1d.logfox.feature.preferences.api.CrashesSort
import com.f0x1d.logfox.feature.preferences.api.data.CrashesSettingsRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class CrashesSettingsRepositoryImpl @Inject constructor(
    private val localDataSource: CrashesSettingsLocalDataSource,
) : CrashesSettingsRepository {

    override fun openCrashesOnStartup(): PreferenceStateFlow<Boolean> = localDataSource.openCrashesOnStartup().asPreferenceStateFlow()

    override fun wrapCrashLogLines(): PreferenceStateFlow<Boolean> = localDataSource.wrapCrashLogLines().asPreferenceStateFlow()

    override fun crashesSortType(): PreferenceStateFlow<CrashesSort> = localDataSource.crashesSortType().asPreferenceStateFlow()

    override fun crashesSortReversedOrder(): PreferenceStateFlow<Boolean> = localDataSource.crashesSortReversedOrder().asPreferenceStateFlow()

    override fun collectingFor(crashTypeName: String): Boolean = localDataSource.collectingFor(crashTypeName)

    override fun collectingForFlow(crashTypeName: String): PreferenceStateFlow<Boolean> =
        localDataSource.collectingForPreference(crashTypeName).asPreferenceStateFlow()

    override fun setCollectingFor(crashTypeName: String, value: Boolean) {
        localDataSource.collectingForPreference(crashTypeName).set(value)
    }

    override fun showingNotificationsFor(crashTypeName: String): Boolean = localDataSource.showingNotificationsFor(crashTypeName)

    override fun showingNotificationsForFlow(crashTypeName: String): PreferenceStateFlow<Boolean> =
        localDataSource.showingNotificationsForPreference(crashTypeName).asPreferenceStateFlow()

    override fun setShowingNotificationsFor(crashTypeName: String, value: Boolean) {
        localDataSource.showingNotificationsForPreference(crashTypeName).set(value)
    }

    override fun useSeparateNotificationsChannelsForCrashes(): PreferenceStateFlow<Boolean> = localDataSource.useSeparateNotificationsChannelsForCrashes().asPreferenceStateFlow()
}
