//package com.example.landmarkremarkapp.nvgrph
//
//import androidx.compose.runtime.Composable
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.navigation
//import androidx.navigation.compose.rememberNavController
//import com.example.landmarkremarkapp.feature.login.LoginScreen
//
//@Composable
//fun NavGraph(
//    startDestination: String
//) {
//    val navController = rememberNavController()
//
//    NavHost(navController = navController, startDestination = startDestination){
//        navigation(
//            route = Route.LoginScreen.route,
//            startDestination = Route.LoginScreen.route
//        ){
//            composable(
//                route = Route.LoginScreen.route
//            ){
//                LoginScreen()
//            }
//        }
//
//        navigation(
//            route = Route.HomeNavigation.route,
//            startDestination = Route.HomeScreen.route
//        ){
//            composable(
//                route = Route.HomeScreen.route
//            ){
//
//            }
//        }
//    }
//}