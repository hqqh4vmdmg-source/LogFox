package com.f0x1d.logfox.presentation

import android.content.Context
import android.content.Intent
import com.f0x1d.logfox.core.compat.startForegroundServiceAvailable
import com.f0x1d.logfox.core.context.hasPermissionToReadLogs
import com.f0x1d.logfox.core.tea.EffectHandler
import com.f0x1d.logfox.feature.logging.api.presentation.LoggingServiceDelegate
import com.f0x1d.logfox.feature.logging.presentation.service.LoggingService
import com.f0x1d.logfox.feature.preferences.api.domain.notifications.SetAskedNotificationsPermissionUseCase
import com.f0x1d.logfox.feature.preferences.api.domain.service.GetStopLoggingOnBackExitUseCase
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

internal class MainEffectHandler @Inject constructor(
    @ApplicationContext private val context: Context,
    private val setAskedNotificationsPermissionUseCase: SetAskedNotificationsPermissionUseCase,
    private val getStopLoggingOnBackExitUseCase: GetStopLoggingOnBackExitUseCase,
    private val loggingServiceDelegate: LoggingServiceDelegate,
) : EffectHandler<MainSideEffect, MainCommand> {

    override suspend fun handle(effect: MainSideEffect, onCommand: suspend (MainCommand) -> Unit) {
        when (effect) {
            MainSideEffect.StartLoggingServiceIfNeeded -> startLoggingServiceIfNeeded(onCommand)

            MainSideEffect.SaveNotificationsPermissionAsked ->
                setAskedNotificationsPermissionUseCase(true)

            MainSideEffect.HandleBackExit -> if (getStopLoggingOnBackExitUseCase()) {
                loggingServiceDelegate.killService()
            } else {
                onCommand(MainCommand.FinishActivityRequested)
            }

            // Handled by Activity
            MainSideEffect.FinishActivity,
            MainSideEffect.OpenSetup -> Unit
        }
    }

    private suspend fun startLoggingServiceIfNeeded(onCommand: suspend (MainCommand) -> Unit) {
        if (!context.hasPermissionToReadLogs) {
            onCommand(MainCommand.ShowSetup)
            return
        }
        val intent = Intent(context, LoggingService::class.java)
        if (startForegroundServiceAvailable) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }
}
