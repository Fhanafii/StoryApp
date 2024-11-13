package com.fhanafi.storyapp.ui.customviews

import android.content.Context
import android.text.InputType
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.widget.doOnTextChanged

class PasswordEditText @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = android.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

    init {
        inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        isFocusable = true
        isClickable = true
        isFocusableInTouchMode = true

        doOnTextChanged { text, _, _, _ ->
            if (text != null && text.length < 8) {
                error = "Password must be at least 8 characters"
            }
        }
    }
}
