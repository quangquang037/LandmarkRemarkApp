package com.example.landmarkremarkapp.activity


import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.landmarkremarkapp.R
import com.example.landmarkremarkapp.feature.list_notes.ListNotesScreen
import com.example.landmarkremarkapp.feature.location.LocationScreen
import com.example.landmarkremarkapp.feature.login.LoginScreen
import com.example.landmarkremarkapp.feature.register.RegisterScreen
import com.example.landmarkremarkapp.navigate.Route
import com.example.landmarkremarkapp.ui.theme.LandmarkRemarkAppTheme
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.rememberCameraPositionState


class MainActivity : ComponentActivity() {
    private val mainViewModel: MainViewModel by viewModels()

    private val permission = arrayOf(
        android.Manifest.permission.ACCESS_COARSE_LOCATION,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    )

    private lateinit var fusedLocation: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private var locationRequired: Boolean = false

    override fun onResume() {
        super.onResume()
        if (locationRequired) {
            startLocationUpdate()
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdate() {
        locationCallback?.let {
            val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 100)
                .setWaitForAccurateLocation(false)
                .setMinUpdateIntervalMillis(3000)
                .setMaxUpdateDelayMillis(100)
                .build()

            fusedLocation?.requestLocationUpdates(locationRequest, it, Looper.getMainLooper())
        }
    }

    override fun onPause() {
        super.onPause()
        locationCallback?.let {
            fusedLocation.removeLocationUpdates(it)
        }

        fusedLocation?.let {
            fusedLocation.removeLocationUpdates(locationCallback)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        MapsInitializer.initialize(this, MapsInitializer.Renderer.LATEST) {

        }
        fusedLocation = LocationServices.getFusedLocationProviderClient(this)

        setContent {
            var navController = rememberNavController()

            var currentLocation by remember {
                mutableStateOf(LatLng(0.toDouble(), 0.toDouble()))
            }

            var cameraPosition = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(
                    currentLocation, 10f
                )
            }

            var cameraPositionState by remember {
                mutableStateOf(cameraPosition)
            }


            locationCallback = object : LocationCallback() {
                override fun onLocationResult(p0: LocationResult) {
                    super.onLocationResult(p0)
                    for (location in p0.locations) {
                        currentLocation = LatLng(location.latitude, location.longitude)
                        mainViewModel.cameraPositionZoom = LatLng(location.latitude,location.longitude)
                        fusedLocation?.let {
                            fusedLocation.removeLocationUpdates(locationCallback)
                        }
                    }
                }
            }

            LandmarkRemarkAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val userState = mainViewModel.users.collectAsState()
                    NavHost(
                        navController = navController,
                        startDestination = if (userState.value.userId.isNullOrEmpty()) {
                            Route.LoginScreen.route
                        } else {
                            Route.HomeScreen.route
                        }
                    ) {
                        composable(route = Route.LoginScreen.route) {
                            LoginScreen(
                                mainViewModel = mainViewModel,
                                registerOnclick = { navController.navigate("register") }
                            )
                        }
                        composable(route = Route.RegisterScreen.route) {
                            RegisterScreen(
                                context = this@MainActivity,
                                mainViewModel = mainViewModel,
                                finishCallBack = {
                                    navController.popBackStack(route = Route.LoginScreen.route, inclusive = false)
                                })
                        }
                        composable(Route.HomeScreen.route) {
                            LocationScreen(context = this@MainActivity,
                                mainViewModel = mainViewModel,
                                userAp = userState.value,
                                currentLocation,
                                permission = permission,
                                callbackLocationRequired = {
                                    locationRequired = true
                                },
                                startLocationUpdate = {
                                    startLocationUpdate()
                                },
                                cameraPositionState = cameraPositionState,
                                onclickListLocation = {
                                    navController.navigate(route = Route.ListNoteScreen.route)
                                })
                        }
                        composable(Route.ListNoteScreen.route) {
                            ListNotesScreen(
                                mainViewModel,
                                findLocationCallBack = {
                                    navController.popBackStack(route = Route.HomeScreen.route, inclusive = false)
                                    mainViewModel.cameraPositionZoom = LatLng(it.latitude,it.longitude)
                                }
                            )
                        }
                    }
                    
                    if(mainViewModel.errorMessDialog.first){
                        AlertDialog(onDismissRequest = {},
                            title = { Text(stringResource(id = R.string.error)) },
                            confirmButton = {
                                Button(onClick = {
                                    mainViewModel.errorMessDialog = Pair(false,null)
                                }) {
                                    Text("Confirm")
                                }
                            },
                            dismissButton = {},
                            text = { Text(mainViewModel.errorMessDialog.second ?: "") })
                    }
                    //Loader()
                }
            }
        }
    }
}



