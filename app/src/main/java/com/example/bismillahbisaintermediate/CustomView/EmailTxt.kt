package com.example.bismillahbisaintermediate.CustomView

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Patterns
import android.widget.EditText
import com.google.android.material.textfield.TextInputLayout

class EmailTxt (context: Context, attrs: AttributeSet) : TextInputLayout(context, attrs) {

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            s?.let {
                if (Patterns.EMAIL_ADDRESS.matcher(it.toString()).matches()) {
                    null
                } else {
                    "Email Tidak Valid"
                }
            }
        }

        override fun afterTextChanged(s: Editable?) {
        }
    }

    fun emailerror(editText: EditText) {
        editText.addTextChangedListener(textWatcher)
    }
}