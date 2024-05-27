package com.example.common.extend

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.example.common.R

private val appFontFamily = FontFamily(
    fonts = listOf(
        Font(
            resId = R.font.poppins_bold,
            weight = FontWeight.W900,
            style = FontStyle.Normal
        ),
        Font(
            resId = R.font.poppins_regular,
            weight = FontWeight.W900,
            style = FontStyle.Italic
        ),
        Font(
            resId = R.font.poppins_semibold,
            weight = FontWeight.W700,
            style = FontStyle.Normal
        ),

))