package com.f0x1d.logfox.feature.filters.impl.di

import com.f0x1d.logfox.feature.filters.api.domain.ClearAllFiltersUseCase
import com.f0x1d.logfox.feature.filters.api.domain.CreateAllFiltersUseCase
import com.f0x1d.logfox.feature.filters.api.domain.CreateFilterUseCase
import com.f0x1d.logfox.feature.filters.api.domain.DeleteFilterUseCase
import com.f0x1d.logfox.feature.filters.api.domain.ExportFiltersToUriUseCase
import com.f0x1d.logfox.feature.filters.api.domain.GetAllEnabledFiltersFlowUseCase
import com.f0x1d.logfox.feature.filters.api.domain.GetAllFiltersFlowUseCase
import com.f0x1d.logfox.feature.filters.api.domain.GetFilterByIdFlowUseCase
import com.f0x1d.logfox.feature.filters.api.domain.ImportFiltersFromUriUseCase
import com.f0x1d.logfox.feature.filters.api.domain.SwitchFilterUseCase
import com.f0x1d.logfox.feature.filters.api.domain.UpdateFilterUseCase
import com.f0x1d.logfox.feature.filters.impl.domain.ClearAllFiltersUseCaseImpl
import com.f0x1d.logfox.feature.filters.impl.domain.CreateAllFiltersUseCaseImpl
import com.f0x1d.logfox.feature.filters.impl.domain.CreateFilterUseCaseImpl
import com.f0x1d.logfox.feature.filters.impl.domain.DeleteFilterUseCaseImpl
import com.f0x1d.logfox.feature.filters.impl.domain.ExportFiltersToUriUseCaseImpl
import com.f0x1d.logfox.feature.filters.impl.domain.GetAllEnabledFiltersFlowUseCaseImpl
import com.f0x1d.logfox.feature.filters.impl.domain.GetAllFiltersFlowUseCaseImpl
import com.f0x1d.logfox.feature.filters.impl.domain.GetFilterByIdFlowUseCaseImpl
import com.f0x1d.logfox.feature.filters.impl.domain.ImportFiltersFromUriUseCaseImpl
import com.f0x1d.logfox.feature.filters.impl.domain.SwitchFilterUseCaseImpl
import com.f0x1d.logfox.feature.filters.impl.domain.UpdateFilterUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface FiltersUseCaseModule {

    @Binds
    fun bindGetAllFiltersFlowUseCase(impl: GetAllFiltersFlowUseCaseImpl): GetAllFiltersFlowUseCase

    @Binds
    fun bindGetAllEnabledFiltersFlowUseCase(impl: GetAllEnabledFiltersFlowUseCaseImpl): GetAllEnabledFiltersFlowUseCase

    @Binds
    fun bindGetFilterByIdFlowUseCase(impl: GetFilterByIdFlowUseCaseImpl): GetFilterByIdFlowUseCase

    @Binds
    fun bindCreateFilterUseCase(impl: CreateFilterUseCaseImpl): CreateFilterUseCase

    @Binds
    fun bindCreateAllFiltersUseCase(impl: CreateAllFiltersUseCaseImpl): CreateAllFiltersUseCase

    @Binds
    fun bindUpdateFilterUseCase(impl: UpdateFilterUseCaseImpl): UpdateFilterUseCase

    @Binds
    fun bindSwitchFilterUseCase(impl: SwitchFilterUseCaseImpl): SwitchFilterUseCase

    @Binds
    fun bindDeleteFilterUseCase(impl: DeleteFilterUseCaseImpl): DeleteFilterUseCase

    @Binds
    fun bindClearAllFiltersUseCase(impl: ClearAllFiltersUseCaseImpl): ClearAllFiltersUseCase

    @Binds
    fun bindExportFiltersToUriUseCase(impl: ExportFiltersToUriUseCaseImpl): ExportFiltersToUriUseCase

    @Binds
    fun bindImportFiltersFromUriUseCase(impl: ImportFiltersFromUriUseCaseImpl): ImportFiltersFromUriUseCase
}
