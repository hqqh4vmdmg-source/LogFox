package com.f0x1d.logfox.core.ui.base.ext

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.Window
import androidx.annotation.AttrRes
import androidx.core.view.WindowCompat
import com.f0x1d.logfox.core.compat.contrastedNavBarAvailable
import com.f0x1d.logfox.core.compat.gesturesAvailable
import com.f0x1d.logfox.core.compat.isAtLeastAndroid15
import com.f0x1d.logfox.core.ui.theme.R

fun Window.enableEdgeToEdge(isContrastEnforced: Boolean = true) {
    WindowCompat.setDecorFitsSystemWindows(this, false)

    WindowCompat.getInsetsController(this, decorView).apply {
        val isLightTheme = context.resolveBoolean(
            attributeResId = androidx.appcompat.R.attr.isLightTheme,
            defaultValue = false,
        )

        isAppearanceLightStatusBars = isLightTheme
        isAppearanceLightNavigationBars = isLightTheme
    }

    if (!isAtLeastAndroid15) {
        navigationBarColor = when {
            !contrastedNavBarAvailable -> context.getColor(
                R.color.transparent_black,
            )

            !gesturesAvailable && isContrastEnforced -> context.getColor(
                R.color.navbar_transparent_background,
            )

            else -> Color.TRANSPARENT
        }
    }
}

private fun Context.resolveAttribute(@AttrRes attributeResId: Int) = TypedValue().let {
    if (theme.resolveAttribute(attributeResId, it, true)) it else null
}

private fun Context.resolveBoolean(@AttrRes attributeResId: Int, defaultValue: Boolean = false): Boolean {
    val value = resolveAttribute(attributeResId) ?: return defaultValue
    return value.type == TypedValue.TYPE_INT_BOOLEAN && value.data != 0
}
