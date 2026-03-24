package com.f0x1d.logfox.core.ui.view

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText

class ExtendedTextWatcher(
    val editText: EditText,
    var enabled: Boolean = true,
    private val doAfterTextChanged: (e: Editable?) -> Unit,
) : TextWatcher {

    init {
        editText.addTextChangedListener(this)
    }

    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit

    override fun afterTextChanged(e: Editable?) {
        if (enabled) {
            doAfterTextChanged(e)
        }
    }

    fun setText(text: String?) {
        if (editText.text?.toString() == text) return

        enabled = false
        editText.setText(text)
        editText.setSelection(editText.length())
        enabled = true
    }
}

fun EditText.applyExtendedTextWatcher(
    doAfterTextChanged: (e: Editable?) -> Unit,
): ExtendedTextWatcher = ExtendedTextWatcher(
    editText = this,
    doAfterTextChanged = doAfterTextChanged,
)
