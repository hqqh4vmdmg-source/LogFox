package com.f0x1d.logfox.core.ui.compose

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.f0x1d.logfox.compose.designsystem.theme.LogFoxTheme
import com.f0x1d.logfox.core.ui.base.DynamicColorAvailabilityProvider
import com.f0x1d.logfox.core.ui.base.ext.enableEdgeToEdge
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent

abstract class BaseComposeBottomSheetFragment : BottomSheetDialogFragment() {

    private val dynamicColorAvailabilityProvider: DynamicColorAvailabilityProvider by lazy {
        EntryPointAccessors
            .fromApplication<BaseComposeBottomSheetFragmentEntryPoint>(requireContext())
            .dynamicColorAvailabilityProvider
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = ComposeView(requireContext()).apply {
        consumeWindowInsets = false
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            LogFoxTheme(
                dynamicColor = dynamicColorAvailabilityProvider.isDynamicColorAvailable(),
            ) {
                Content()
            }
        }
    }

    @Composable
    abstract fun Content()

    @SuppressLint("RestrictedApi")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
        (super.onCreateDialog(savedInstanceState) as BottomSheetDialog).apply {
            window?.enableEdgeToEdge(isContrastEnforced = false)
            behavior.skipCollapsed = true
            behavior.state = BottomSheetBehavior.STATE_EXPANDED
            behavior.disableShapeAnimations()
        }

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    internal interface BaseComposeBottomSheetFragmentEntryPoint {
        val dynamicColorAvailabilityProvider: DynamicColorAvailabilityProvider
    }
}
