package com.example.data.models

data class NotesData(
    var id: String = "",
    var headNote: String = "",
    var userName: String = "",
    var titleNotes: String = "",
    var descriptionNotes: String = "",
    var location: Location = Location(),
    var time: String = "",
    var userEdit: Boolean = false
)