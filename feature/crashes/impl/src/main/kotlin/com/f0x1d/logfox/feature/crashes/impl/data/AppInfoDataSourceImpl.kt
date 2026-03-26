package com.f0x1d.logfox.feature.crashes.impl.data

import android.content.Context
import androidx.core.content.pm.PackageInfoCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class AppInfoDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context,
) : AppInfoDataSource {

    override fun getAppName(packageName: String): String? = with(context.packageManager) {
        runCatching {
            getPackageInfo(packageName, 0).applicationInfo
                ?.let(::getApplicationLabel)
                ?.toString()
        }.getOrNull()
    }

    override fun getAppInfo(packageName: String): AppInfo = with(context.packageManager) {
        val packageInfo = runCatching { getPackageInfo(packageName, 0) }.getOrNull()
        AppInfo(
            appName = packageInfo?.applicationInfo
                ?.let(::getApplicationLabel)
                ?.toString(),
            packageName = packageName,
            versionName = packageInfo?.versionName,
            versionCode = packageInfo?.let(PackageInfoCompat::getLongVersionCode),
        )
    }
