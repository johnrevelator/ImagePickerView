package ru.vvdev.imagepickerview

import android.graphics.drawable.Drawable

data class AddAttr(
        val drawable: Drawable,
        val text: String,
        val textSize: Int,
        val textStyle: Int,
        val imageBack: Int
)