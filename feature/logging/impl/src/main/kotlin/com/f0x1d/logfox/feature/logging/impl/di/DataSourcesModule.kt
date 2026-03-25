package com.f0x1d.logfox.feature.logging.impl.di

import com.f0x1d.logfox.feature.logging.impl.data.LogsBufferDataSource
import com.f0x1d.logfox.feature.logging.impl.data.LogsBufferDataSourceImpl
import com.f0x1d.logfox.feature.logging.impl.data.LogsDataSource
import com.f0x1d.logfox.feature.logging.impl.data.LogsDataSourceImpl
import com.f0x1d.logfox.feature.logging.impl.data.SearchDataSource
import com.f0x1d.logfox.feature.logging.impl.data.SearchDataSourceImpl
import com.f0x1d.logfox.feature.logging.impl.data.SelectedLogLinesDataSource
import com.f0x1d.logfox.feature.logging.impl.data.SelectedLogLinesDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal interface DataSourcesModule {

    @Binds
    fun bindLogsBufferDataSource(impl: LogsBufferDataSourceImpl): LogsBufferDataSource

    @Binds
    fun bindLogsDataSource(impl: LogsDataSourceImpl): LogsDataSource

    @Binds
    fun bindSearchDataSource(impl: SearchDataSourceImpl): SearchDataSource

    @Binds
    fun bindSelectedLogLinesDataSource(impl: SelectedLogLinesDataSourceImpl): SelectedLogLinesDataSource
}
