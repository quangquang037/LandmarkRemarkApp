package com.example.landmarkremarkapp.feature.list_notes

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.example.common.compoment.SearchView
import com.example.common.compoment.ViewLocationItem
import com.example.common.dialog.DialogDeleteNote
import com.example.common.dialog.DialogNote
import com.example.common.dialog.DialogNoteState
import com.example.data.models.Location
import com.example.data.models.NotesData
import com.example.domain.enum.SelectLocationStatus
import com.example.landmarkremarkapp.activity.MainActivity
import com.example.landmarkremarkapp.activity.MainViewModel

@Composable
fun ListNotesScreen(
    mainViewModel: MainViewModel,
    findLocationCallBack: (location: Location) -> Unit
) {

    var listNote = remember { mainViewModel.createListNotesData() }
    val selectNoteItem = remember {
        mutableStateOf(NotesData())
    }
    var openDialog by remember { mutableStateOf(false) }
    var openDialogEditNote by remember { mutableStateOf(false) }
    var openDialogDeleteNote by remember { mutableStateOf(false) }
    val textState = remember { mutableStateOf(TextFieldValue("")) }

    Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
        val listNoteState = mainViewModel.listLocationData.collectAsState()
        listNoteState.run {
            listNote = mainViewModel.createListNotesData()
        }
        Column(modifier = Modifier.fillMaxSize()) {
            SearchView(state = textState, placeHolder = "Search..." )
            val searchedText = textState.value.text
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                items(listNote.filter {
                    it.userName.contains(searchedText, ignoreCase = true)||
                            it.titleNotes.contains(searchedText, ignoreCase = true)
                }) {
                    ViewLocationItem(
                        item = it,
                        selectItems = {status, note ->
                            selectNoteItem.value = note
                            when(status){
                                SelectLocationStatus.FIND_LOCATION -> { findLocationCallBack.invoke(note.location)}
                                SelectLocationStatus.SHOW_NOTE ->{ openDialog = true }
                                SelectLocationStatus.EDIT_NOTE ->{ openDialogEditNote = true}
                                SelectLocationStatus.DELETE_NOTE ->{openDialogDeleteNote = true}
                            }
                        },
                        )

                }
            }
        }
        if (openDialog) {
            DialogNote(
                onConfirmation = {
                    openDialog = false
                },
                dialogTitle = selectNoteItem.value.titleNotes,
                dialogText = selectNoteItem.value.descriptionNotes
            )
        }

        if(openDialogEditNote){
            DialogNoteState(
                title = selectNoteItem.value.titleNotes,description = selectNoteItem.value.descriptionNotes,
                acceptOnclick = { title, description ->
                   selectNoteItem.value.apply {

                       if (!titleNotes.contentEquals(title) && !descriptionNotes.contentEquals(description.text)){
                           titleNotes = title
                           descriptionNotes = description.text
                           openDialogEditNote = false
                           mainViewModel.updateNoteInUser(selectNoteItem.value)
                       }else{
                           mainViewModel.errorMessDialog  = Pair(true,"Input new title or description")
                       }
                   }

                },
                closeOnclick = {
                    openDialogEditNote = false
                }
            )
        }

        if(openDialogDeleteNote){
            DialogDeleteNote(
                onConfirmation = {
                    openDialogDeleteNote = false
                    mainViewModel.deleteNoteInUser(selectNoteItem.value.headNote)
                },
                onDismiss = {
                    openDialogDeleteNote = false
                })
        }
    }

}

