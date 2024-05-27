package com.example.common.compoment

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.common.R

@Composable
fun SearchView(
    modifier: Modifier = Modifier,
    state: MutableState<TextFieldValue>,
    placeHolder: String
) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
    ) {

        Icon(
            painter = painterResource(id = R.drawable.ic_search),
            contentDescription = "search",
            modifier = Modifier.size(25.dp)
        )
        Spacer(modifier = Modifier.size(16.dp))
        BasicTextField(
            value = state.value,
            onValueChange = {
                state.value = it },
            modifier = Modifier
                .background(shape = RoundedCornerShape(10.dp), color = Color.LightGray)
                .fillMaxWidth()
                .padding(16.dp),
        )
    }
}
