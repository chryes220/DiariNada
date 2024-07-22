package com.example.diarinada

import android.content.Context
import android.graphics.Typeface
import android.text.style.TypefaceSpan
import androidx.core.content.res.ResourcesCompat

class MenuTypeface (private val context: Context): TypefaceSpan("") {
    private var newType = ResourcesCompat.getFont(context, R.font.diarinadafonts)

    fun CustomTypefaceSpan(family: String, type: Typeface) {
        newType = type
    }
}