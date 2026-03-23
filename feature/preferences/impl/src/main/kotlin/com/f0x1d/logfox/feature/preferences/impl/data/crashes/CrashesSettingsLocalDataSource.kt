package com.f0x1d.logfox.feature.preferences.impl.data.crashes

import com.f0x1d.logfox.feature.preferences.api.CrashesSort
import com.fredporciuncula.flow.preferences.Preference

internal interface CrashesSettingsLocalDataSource {
    fun openCrashesOnStartup(): Preference<Boolean>
    fun wrapCrashLogLines(): Preference<Boolean>

    fun crashesSortType(): Preference<CrashesSort>
    fun crashesSortReversedOrder(): Preference<Boolean>

    fun collectingFor(crashTypeName: String): Boolean
    fun collectingForPreference(crashTypeName: String): Preference<Boolean>
    fun showingNotificationsFor(crashTypeName: String): Boolean
    fun showingNotificationsForPreference(crashTypeName: String): Preference<Boolean>

    fun useSeparateNotificationsChannelsForCrashes(): Preference<Boolean>
}
