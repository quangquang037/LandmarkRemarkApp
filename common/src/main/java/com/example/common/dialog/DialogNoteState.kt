package com.example.common.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.common.R
import com.example.common.compoment.ButtonDialog
import com.example.common.compoment.CustomTextField


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogNoteState(
    title: String? = null, description: String?? = null,
    acceptOnclick: (title: String, description: TextFieldValue) -> Unit,
    closeOnclick: () -> Unit
) {
    var titleValue by rememberSaveable { mutableStateOf(title ?: "") }
    var inputText = remember { mutableStateOf(TextFieldValue(description ?: "")) }

    var fieldValidation by remember { mutableStateOf(false) }
    Dialog(onDismissRequest = { }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(350.dp)
                .padding(
                    start = 10.dp,
                    top = 5.dp,
                    end = 10.dp,
                    bottom = 5.dp
                ),
            shape = RoundedCornerShape(16.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                TextField(
                    value = titleValue,
                    onValueChange = {
                        titleValue = it
                    },
                    label = { Text(stringResource(id = R.string.title)) }
                )
                CustomTextField(
                    label = stringResource(id = R.string.description),
                    textInput = inputText
                )
                Row(
                    modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End
                ) {
                    ButtonDialog(text = "Cancel",
                        color = Color.Red,
                        onClick = { closeOnclick.invoke() })
                    ButtonDialog(text = "Confirm",
                        color = Color.Green,
                        onClick = {
                            if (titleValue.isNullOrEmpty() || inputText.value.text.isNullOrEmpty()) {
                                fieldValidation = true
                            } else {
                                fieldValidation = false
                                acceptOnclick.invoke(titleValue, inputText.value)
                            }
                        }
                    )
                }

                if (fieldValidation) {
                    Text(
                        color = Color.Red,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        text = stringResource(id = R.string.empty_field_error)
                    )
                }
            }
        }
    }
}

