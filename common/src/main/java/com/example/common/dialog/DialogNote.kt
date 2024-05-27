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
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.common.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogNote(
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
) {
    AlertDialog(
        icon = {
            Icon(
                modifier = Modifier.size(20.dp),
                painter = painterResource(id = R.drawable.note_icon_popup), contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {},
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Close")
            }
        },
    )
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun DialogNotePreview() {
    DialogNote(
        onConfirmation = { /*TODO*/ },
        dialogTitle = "Title" ,
        dialogText = "Manchester United can move within one FA Cup of record winners Arsenal with a win today"
    )
}