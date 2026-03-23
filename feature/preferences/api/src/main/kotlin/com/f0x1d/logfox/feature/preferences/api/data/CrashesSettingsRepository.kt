package com.f0x1d.logfox.feature.preferences.api.data

import com.f0x1d.logfox.core.preferences.api.PreferenceStateFlow
import com.f0x1d.logfox.feature.preferences.api.CrashesSort

interface CrashesSettingsRepository {
    fun openCrashesOnStartup(): PreferenceStateFlow<Boolean>
    fun wrapCrashLogLines(): PreferenceStateFlow<Boolean>

    fun crashesSortType(): PreferenceStateFlow<CrashesSort>
    fun crashesSortReversedOrder(): PreferenceStateFlow<Boolean>

    fun collectingFor(crashTypeName: String): Boolean
    fun collectingForFlow(crashTypeName: String): PreferenceStateFlow<Boolean>
    fun setCollectingFor(crashTypeName: String, value: Boolean)
    fun showingNotificationsFor(crashTypeName: String): Boolean
    fun showingNotificationsForFlow(crashTypeName: String): PreferenceStateFlow<Boolean>
    fun setShowingNotificationsFor(crashTypeName: String, value: Boolean)

    fun useSeparateNotificationsChannelsForCrashes(): PreferenceStateFlow<Boolean>
}
