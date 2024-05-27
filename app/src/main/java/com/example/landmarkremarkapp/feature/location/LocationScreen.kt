package com.example.landmarkremarkapp.feature.location

import android.content.Context
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.common.compoment.ButtonCustom
import com.example.common.dialog.DialogNoteState
import com.example.data.models.UserApp
import com.example.landmarkremarkapp.activity.MainViewModel
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState


@Composable
fun LocationScreen(
    context: Context,
    mainViewModel: MainViewModel,
    userAp: UserApp,
    currentLocation: LatLng,
    permission: Array<String>,
    callbackLocationRequired: () -> Unit = {},
    startLocationUpdate: () -> Unit = {},
    cameraPositionState: CameraPositionState,
    onclickListLocation: () -> Unit
){

    val launchMultiplePermissions = rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){permissionMaps->
        val areGranted = permissionMaps.values.reduce {acc,next -> acc && next}
        if(areGranted){
            callbackLocationRequired.invoke()
            startLocationUpdate.invoke()
            Toast.makeText(context,"Permission Granted",Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(context,"Permission Denied",Toast.LENGTH_SHORT).show()
        }
    }

    var makerBitmap = context.resources.getDrawable(com.example.common.R.drawable.user_vector).toBitmap(100,100)


    Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)){
        val openDialog = remember { mutableStateOf(false) }
        val locationData = mainViewModel.listLocationData.collectAsState()

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState )
        {
            locationData.value.forEachIndexed { index, it ->
                Marker(
                    state = MarkerState(
                        position = LatLng(it.location.latitude, it.location.longitude)
                    ),
                    title = it.name,
                    snippet = it.titleLocation,
                    icon = BitmapDescriptorFactory.fromBitmap(makerBitmap)
                )
            }

            Marker(
                state = MarkerState(
                    position = currentLocation
                ),
                title = userAp.name,
                snippet = "You're here !!!"
            )

            mainViewModel.cameraPositionZoom.apply {
                cameraPositionState.position = CameraPosition.fromLatLngZoom(LatLng(latitude,longitude),15f)
            }
        }
        
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(color = Color.Red,
                text = "Location: ${currentLocation.latitude} - ${currentLocation.longitude}")
            Row(modifier = Modifier.fillMaxWidth()) {
                ButtonCustom(text = "Save note", onClick = {
                    openDialog.value = true
                })
                ButtonCustom(text = "List note", onClick = {
                   onclickListLocation.invoke()
                })
                ButtonCustom(text = "Get location", onClick = {
                    if (permission.all {
                            ContextCompat.checkSelfPermission(context,it) == PackageManager.PERMISSION_GRANTED
                        })
                    {
                        //get location
                        startLocationUpdate.invoke()
                    }else{
                        launchMultiplePermissions.launch(permission)
                    }
                })

            }
        }

        if(openDialog.value){
            DialogNoteState(
                acceptOnclick = {title, description ->
                    mainViewModel.pushDescriptionLocation(
                        title,description.text,userAp,currentLocation)
                    openDialog.value = false
                },
                closeOnclick = {
                    openDialog.value  = false},
            )
        }

        if(locationData.value.isEmpty()){
            mainViewModel.getAllLocation()
        }
    }
}


