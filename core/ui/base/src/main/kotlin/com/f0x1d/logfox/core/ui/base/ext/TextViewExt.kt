package com.f0x1d.logfox.core.ui.base.ext

import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

fun TextView.doAfterTextChanged(
    fragment: Fragment,
    callback: (Editable?) -> Unit,
) {
    val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
        override fun afterTextChanged(s: Editable) = callback(s)
    }

    fragment.viewLifecycleOwner.lifecycle.addObserver(
        object : DefaultLifecycleObserver {
            override fun onResume(owner: LifecycleOwner) {
                addTextChangedListener(textWatcher)
            }

            override fun onPause(owner: LifecycleOwner) {
                removeTextChangedListener(textWatcher)
            }
        },
    )
}
