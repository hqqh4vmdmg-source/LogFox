package com.f0x1d.logfox.feature.crashes.impl.di

import com.f0x1d.logfox.feature.crashes.api.domain.CheckAppDisabledUseCase
import com.f0x1d.logfox.feature.crashes.api.domain.GetAllDisabledAppsFlowUseCase
import com.f0x1d.logfox.feature.crashes.api.domain.IsAppDisabledFlowUseCase
import com.f0x1d.logfox.feature.crashes.impl.domain.CheckAppDisabledUseCaseImpl
import com.f0x1d.logfox.feature.crashes.impl.domain.GetAllDisabledAppsFlowUseCaseImpl
import com.f0x1d.logfox.feature.crashes.impl.domain.IsAppDisabledFlowUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface DisabledAppsUseCaseModule {

    @Binds
    fun bindGetAllDisabledAppsFlowUseCase(impl: GetAllDisabledAppsFlowUseCaseImpl): GetAllDisabledAppsFlowUseCase

    @Binds
    fun bindIsAppDisabledFlowUseCase(impl: IsAppDisabledFlowUseCaseImpl): IsAppDisabledFlowUseCase

    @Binds
    fun bindCheckAppDisabledUseCase(impl: CheckAppDisabledUseCaseImpl): CheckAppDisabledUseCase
}
