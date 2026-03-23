package com.f0x1d.logfox.feature.preferences.api.domain.crashes

import kotlinx.coroutines.flow.Flow

interface GetCollectingForCrashTypeFlowUseCase {
    operator fun invoke(crashTypeName: String): Flow<Boolean>
}
