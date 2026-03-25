package com.f0x1d.logfox.feature.logging.presentation.service

import com.f0x1d.logfox.core.tea.ReduceResult
import com.f0x1d.logfox.core.tea.Reducer
import com.f0x1d.logfox.core.tea.noSideEffects
import com.f0x1d.logfox.core.tea.withSideEffects
import javax.inject.Inject

internal class LoggingServiceReducer @Inject constructor() :
    Reducer<LoggingServiceState, LoggingServiceCommand, LoggingServiceSideEffect> {

    override fun reduce(
        state: LoggingServiceState,
        command: LoggingServiceCommand,
    ): ReduceResult<LoggingServiceState, LoggingServiceSideEffect> = when (command) {
        is LoggingServiceCommand.StartLogging -> if (state.isLoggingActive) {
            state.noSideEffects()
        } else {
            state.withSideEffects(LoggingServiceSideEffect.SelectTerminal)
        }

        is LoggingServiceCommand.TerminalSelected -> state.copy(
            currentTerminal = command.terminal,
            isLoggingActive = true,
        ).withSideEffects(
            LoggingServiceSideEffect.StartLogCollection(command.terminal),
            LoggingServiceSideEffect.ScheduleLogUpdates,
        )

        is LoggingServiceCommand.StopLogging -> state.currentTerminal?.let { terminal ->
            state.copy(isLoggingActive = false).withSideEffects(
                LoggingServiceSideEffect.StopLogCollection,
                LoggingServiceSideEffect.CancelLogUpdates,
                LoggingServiceSideEffect.NotifyLoggingStopped,
                LoggingServiceSideEffect.ExitTerminal(terminal),
            )
        } ?: state.copy(isLoggingActive = false).noSideEffects()

        is LoggingServiceCommand.RestartLogging -> state.currentTerminal?.let { terminal ->
            state.copy(isLoggingActive = false).withSideEffects(
                LoggingServiceSideEffect.StopLogCollection,
                LoggingServiceSideEffect.CancelLogUpdates,
                LoggingServiceSideEffect.ExitTerminal(terminal),
                LoggingServiceSideEffect.SelectTerminal,
            )
        } ?: state.withSideEffects(LoggingServiceSideEffect.SelectTerminal)

        is LoggingServiceCommand.ClearLogs -> state.withSideEffects(LoggingServiceSideEffect.ClearLogs)

        is LoggingServiceCommand.KillService -> {
            val terminal = state.currentTerminal
            val sideEffects = buildList {
                add(LoggingServiceSideEffect.StopLogCollection)
                add(LoggingServiceSideEffect.CancelLogUpdates)
                add(LoggingServiceSideEffect.NotifyLoggingStopped)
                if (terminal != null) add(LoggingServiceSideEffect.ExitTerminal(terminal))
                add(LoggingServiceSideEffect.PerformKillService)
            }
            state.copy(isLoggingActive = false).withSideEffects(*sideEffects.toTypedArray())
        }

        is LoggingServiceCommand.TerminalFallback -> {
            val sideEffects = buildList {
                state.currentTerminal?.let { add(LoggingServiceSideEffect.ExitTerminal(it)) }
                add(LoggingServiceSideEffect.StartLogCollection(command.newTerminal))
            }
            state.copy(currentTerminal = command.newTerminal).withSideEffects(*sideEffects.toTypedArray())
        }

        is LoggingServiceCommand.LoggingError -> state.withSideEffects(
            LoggingServiceSideEffect.HandleLoggingError(
                error = command.error,
                terminal = command.terminal,
            ),
        )

        is LoggingServiceCommand.LoggingFlowCompleted ->
            // Logging flow completed normally (e.g., terminal process ended)
            // Restart the logging flow if still active
            state.currentTerminal?.takeIf { state.isLoggingActive }?.let { terminal ->
                state.withSideEffects(LoggingServiceSideEffect.StartLogCollection(terminal))
            } ?: state.noSideEffects()

        is LoggingServiceCommand.ShowToast -> state.withSideEffects(
            LoggingServiceSideEffect.ShowToast(command.message),
        )
    }
}
