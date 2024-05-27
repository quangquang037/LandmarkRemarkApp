package com.example.landmarkremarkapp.feature.register

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import android.widget.Toast
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.common.FormModelStep
import com.example.common.compoment.ButtonCustom
import com.example.domain.entiity.RegisterEntity
import com.example.domain.enum.RegisterEnum
import com.example.landmarkremarkapp.R
import com.example.landmarkremarkapp.activity.MainViewModel
import com.example.landmarkremarkapp.ui.theme.LandmarkRemarkAppTheme

@Composable
fun  RegisterScreen(
    context: Context,
    mainViewModel: MainViewModel,
    finishCallBack: () -> Unit
){

    var emailValue by rememberSaveable { mutableStateOf("") }
    var passwordValue by rememberSaveable { mutableStateOf("") }
    var nameValue by rememberSaveable { mutableStateOf("") }
    var addressValue by rememberSaveable { mutableStateOf("") }

    val listFieldCom : MutableMap<String, FormModelStep> = newInt()


    Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)){
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center) {
            Text(text = stringResource(id = R.string.register_page),
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.ExtraBold))
            Spacer(modifier = Modifier.height(30.dp))
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
            Spacer(modifier = Modifier.height(10.dp))
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
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center){
                TextField(
                    value = nameValue,
                    onValueChange = {
                        nameValue = it
                    },
                    singleLine = true,
                    label = { Text(stringResource(id = R.string.userName)) }
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center){
                TextField(
                    value = addressValue,
                    onValueChange = {
                        addressValue = it
                    },
                    singleLine = true,
                    label = { Text(stringResource(id = R.string.address)) }
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center){
                ButtonCustom(text = stringResource(id = R.string.register), onClick = {

                    listFieldCom.apply {
                        this[RegisterEnum.EMAIL.toLocalization()]?.value = emailValue
                        this[RegisterEnum.Password.toLocalization()]?.value = passwordValue
                        this[RegisterEnum.Name.toLocalization()]?.value = nameValue
                        this[RegisterEnum.Address.toLocalization()]?.value = addressValue
                    }

                    mainViewModel.registerAccount(listFieldCom)
                    mainViewModel.registerFinnish = {
                        finishCallBack.invoke()
                    }
                })
            }
        }
    }
}

 fun newInt(): MutableMap<String,FormModelStep>{
    val data : MutableMap<String,FormModelStep> = mutableMapOf()
    data.apply {
        this[RegisterEnum.EMAIL.toLocalization()] = FormModelStep()
        this[RegisterEnum.Password.toLocalization()] = FormModelStep()
        this[RegisterEnum.Name.toLocalization()] = FormModelStep()
        this[RegisterEnum.Address.toLocalization()] = FormModelStep()
    }
    return data
}


@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun RegisterScreenPreview() {
    LandmarkRemarkAppTheme {

    }
}