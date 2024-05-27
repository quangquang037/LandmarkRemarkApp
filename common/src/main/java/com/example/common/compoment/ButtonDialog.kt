package com.example.common.compoment

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


@Composable
fun ButtonDialog(
    text: String,
    color: Color,
    onClick: () -> Unit
){
    TextButton(modifier = Modifier
        .padding(
            start = 15.dp,
            end = 15.dp,
            top = 5.dp,
            bottom = 5.dp
        )
        .background(
            color = color,
            shape = CircleShape
        )
        ,onClick = {
            onClick.invoke()
        }
    ) {
        Text(text = text,
            color = Color.White
        )
    }
}

