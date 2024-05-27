package com.example.common.dialog

import android.content.res.Configuration
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.common.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogDeleteNote(
    onConfirmation: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        icon = {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(id = R.drawable.delete_icon_popup),
                contentDescription = "Example Icon"
            )
        },
        title = {
            Text(text = stringResource(id = R.string.delete_note) )
        },
        text = {
            Text(text = stringResource(id = R.string.content_delete))
        },
        onDismissRequest = {},
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Confirm")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismiss()
                }
            ) {
                Text("Dismiss")
            }
        }
    )
}
