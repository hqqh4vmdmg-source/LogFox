package com.f0x1d.logfox.feature.preferences.api.domain.logs

interface SetResumeLoggingWithBottomTouchUseCase {
    operator fun invoke(resumeWithTouch: Boolean)
}
