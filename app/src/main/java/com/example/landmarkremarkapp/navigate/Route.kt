package com.example.landmarkremarkapp.navigate

sealed class Route(
    val route: String
){
    object LoginScreen : Route(route = "login")
    object RegisterScreen: Route(route = "register")
    object HomeScreen: Route(route = "home")
    object ListNoteScreen: Route(route = "list note")
}