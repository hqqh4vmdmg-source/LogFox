package com.f0x1d.logfox.feature.preferences.api.domain.logs

interface SetLogsExpandedUseCase {
    operator fun invoke(expanded: Boolean)
}
