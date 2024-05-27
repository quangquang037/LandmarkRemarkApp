package com.example.common.compoment

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.getSelectedText
import androidx.compose.ui.text.input.getTextBeforeSelection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.common.R


@Composable
fun CustomTextField (
    label: String,
    textInput:MutableState<TextFieldValue>
) {
    TextField(
        label = { Text(label) },
        value = textInput.value , onValueChange = {
            textInput.value = it
        },
        modifier = Modifier.fillMaxWidth().heightIn(min = 120.dp) ,
    )
}
private fun changeSegmentColor(textFVal: TextFieldValue):TextFieldValue{
    val txtAnnotatedBuilder = AnnotatedString.Builder()
    val realStartIndex = textFVal.getTextBeforeSelection(textFVal.text.length).length
    val endIndex = realStartIndex + textFVal.getSelectedText().length
    txtAnnotatedBuilder.append(textFVal.annotatedString)
    val myStyle = SpanStyle(
        color = Color.Red ,
        fontSize = 16.sp ,
        background = Color.Green
    )
    txtAnnotatedBuilder.addStyle(myStyle ,realStartIndex ,endIndex)
    return textFVal.copy(annotatedString = txtAnnotatedBuilder.toAnnotatedString())
}