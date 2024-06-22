package com.example.bismillahbisaintermediate.CustomView

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.widget.EditText
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class PasswordTxt(context: Context, attrs: AttributeSet) : TextInputLayout(context, attrs) {

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            s?.let {
                error = if (it.length < 8) {
                    "Password Kurang dari 8"
                } else {
                    null
                }
            }
        }

        override fun afterTextChanged(s: Editable?) {
        }
    }

    fun passworderror(editText: EditText) {
        editText.addTextChangedListener(textWatcher)
    }
}