package com.example.landmarkremarkapp.feature.login

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.common.compoment.ButtonCustom
import com.example.landmarkremarkapp.R
import com.example.landmarkremarkapp.activity.MainViewModel
import com.example.landmarkremarkapp.ui.theme.LandmarkRemarkAppTheme

@Composable
fun LoginScreen(
    mainViewModel: MainViewModel,
    registerOnclick: () -> Unit ={}
    ){
    var emailValue by rememberSaveable { mutableStateOf("pducquang037@gmail.com") }
    var passwordValue by rememberSaveable { mutableStateOf("Quang_168") }

    Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)){
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(text = stringResource(id = R.string.app_name), style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.ExtraBold))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center){
                TextField(
                    value = emailValue,
                    onValueChange = {
                        emailValue = it
                    },
                    singleLine = true,
                    label = { Text(stringResource(id = R.string.email)) }
                )
            }
            Spacer(modifier = Modifier.height(30.dp))
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center){
                TextField(
                    value = passwordValue,
                    onValueChange = {
                        passwordValue = it
                    },
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation(),
                    label = { Text(stringResource(id = R.string.password)) }
                )
            }
            Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                ButtonCustom(text = stringResource(id = R.string.login), onClick = {
                     mainViewModel.loginUser(emailValue,passwordValue)
                })
                ButtonCustom(text = stringResource(id = R.string.register), onClick = {
                    registerOnclick.invoke()
                })
            }
        }
    }
}




@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun LoginScreenPreview(){
    LandmarkRemarkAppTheme {

    }
}