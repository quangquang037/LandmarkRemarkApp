package com.example.landmarkremarkapp.activity

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.common.FormModelStep
import com.example.data.mapper.LocationMapper
import com.example.data.models.LocationData
import com.example.data.models.LocationUser
import com.example.data.models.NotesData
import com.example.data.models.UserApp
import com.example.domain.entiity.LocationDescriptionEntity
import com.example.domain.entiity.LocationEntity
import com.example.domain.entiity.RegisterEntity
import com.example.domain.enum.RegisterEnum
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.regex.Pattern


class MainViewModel : ViewModel() {

    private val _users = MutableStateFlow(UserApp())
    var users = _users.asStateFlow()

    private val database = Firebase.database
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()


    private val _listLocationData: MutableStateFlow<MutableList<LocationData>> = MutableStateFlow(mutableListOf())
    var listLocationData = _listLocationData.asStateFlow()

    var listLocationUser: MutableList<LocationUser> = mutableListOf()

    var cameraPositionZoom by mutableStateOf(LatLng(0.0,0.0))
    var errorMessDialog by mutableStateOf(Pair<Boolean,String?>(false,null))

    var registerFinnish: () -> Unit = { }

    fun loginUser(email: String, password: String) {
        if (email.isNullOrEmpty() || password.isNullOrEmpty()) {
            errorMessDialog = Pair(true,"Email or Password is empty")
            return
        }else{
            if(!EMAIL_ADDRESS_PATTERN.matcher(email).matches()) { errorMessDialog = Pair(true,"Email in valid")
                return }
            viewModelScope.launch {
                createUserFirebaseAuth(email,password)
            }
        }

    }

    private val EMAIL_ADDRESS_PATTERN: Pattern = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )

    private suspend fun createUserFirebaseAuth(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Login successful
                    val user = auth.currentUser

                    val userApp = database.getReference("User").child(user?.uid.toString())
                    userApp.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                val item = snapshot.getValue(UserApp::class.java)
                                _users.value = item ?: UserApp()
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                    })

                } else {
                    // Login failed
                    errorMessDialog = Pair(true,"User not found")
                }
            }.await()
    }


    fun registerAccount(listFieldCom: MutableMap<String, FormModelStep>){
        checkValueIsValid(
            listFieldCom,
            success = {
                val registerUseCase = RegisterEntity(
                    email = listFieldCom[RegisterEnum.EMAIL.toLocalization()]?.value
                        ?: "",
                    password = listFieldCom[RegisterEnum.Password.toLocalization()]?.value
                        ?: "",
                    name = listFieldCom[RegisterEnum.Name.toLocalization()]?.value
                        ?: "",
                    address = listFieldCom[RegisterEnum.Address.toLocalization()]?.value
                        ?: ""
                )
                viewModelScope.launch {
                    registerUserInFirebase(registerUseCase)
                }
            }, error = {
                var fieldError = ""
                for ((k, v) in listFieldCom) {
                    if (!v.isValid) {
                        fieldError += "${k}: ${v.errorMess} \n"
                    }
                }
                errorMessDialog = Pair(true,fieldError)
            }
        )
    }

    private fun checkValueIsValid(
        listForm: MutableMap<String, FormModelStep>,
        success: () -> Unit,
        error: () -> Unit
    ) {
        listForm.mapKeys {
            val value = it.value.value.isNullOrEmpty()
            if (value) {
                it.value.isValid = false
                it.value.errorMess = "This field is required"
            } else {
                it.value.isValid = true
                it.value.errorMess = ""
            }

            when(it.key){
                RegisterEnum.EMAIL.toLocalization() ->{
                    if(!EMAIL_ADDRESS_PATTERN.matcher(it.value.value).matches()){
                        it.value.isValid = false
                        it.value.errorMess = "this not email"
                    }else{
                        it.value.isValid = true
                        it.value.errorMess = ""
                    }
                }
            }

            return@mapKeys
        }

        val isValid = listForm.map {
            it.value.isValid
        }.filter {
            !it
        }

        if (isValid?.size == 0) {
            success.invoke()
            return
        }

        error.invoke()
    }

    private suspend fun registerUserInFirebase(registerUseCase: RegisterEntity) {
        auth.createUserWithEmailAndPassword(registerUseCase.email, registerUseCase.password)
            .addOnCompleteListener { task: Task<AuthResult> ->
                if (task.isSuccessful) {
                    //Registration OK
                    this.auth.currentUser?.let {
                        val myRef = database.getReference("User").child(it.uid)
                        myRef.setValue(
                            UserApp(
                                userId = it.uid,
                                name = registerUseCase.name,
                                email = registerUseCase.email,
                                address = registerUseCase.address
                            ), DatabaseReference.CompletionListener { error, ref ->
                                if (ref.child(it.uid) != null) {
                                    registerFinnish.invoke()
                                } else {
                                    errorMessDialog = Pair(true,error.toString())
                                }
                            }
                        )
                    }
                } else {
                    //Registration error

                }
            }.await()
    }

    fun pushDescriptionLocation(
        title: String,
        description: String,
        userApp: UserApp,
        location: LatLng
    ) {
        var formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
        var item = LocationDescriptionEntity(
            id = "${userApp.userId}/${System.currentTimeMillis()}",
            name = userApp.name,
            titleLocation = title, description = description,
            location = LocationEntity(location.longitude, location.latitude),
            time = formatter.format(LocalDateTime.now())
        )

        val myRef = database.getReference("Location").child(userApp.userId).push()
        myRef.setValue(item)
    }

    fun getAllLocation() {
        val myRef = database.getReference("Location")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    //mapper data
                    val data = LocationMapper.mapper(snapshot.children)
                    listLocationUser = data
                    getLocationWithUser(data)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }

    fun getLocationWithUser(listItem: MutableList<LocationUser>) {
        var listLocation: MutableList<LocationData> = mutableListOf()
        listItem.forEach {
            val ut = it.useValue.map { item->
                item.locationData
            }
            listLocation.addAll(ut)
        }
        _listLocationData.value = listLocation
    }


    fun createListNotesData(): MutableList<NotesData>{
        val listNotes: MutableList<NotesData> = mutableListOf()
        listLocationUser.forEach { user ->
            user.useValue.forEach {
                listNotes.add(
                    NotesData().also { item ->
                        if (user.userKey == users.value.userId) {
                            item.userEdit = true
                        }
                        item.apply {
                            headNote = it.itemKey
                            id = it.locationData.id
                            userName = it.locationData.name
                            titleNotes = it.locationData.titleLocation
                            descriptionNotes = it.locationData.descriptionLocation
                            location = it.locationData.location
                            time = it.locationData.time
                        }
                    }
                )
            }
        }
        return listNotes
    }

    fun updateNoteInUser(item: NotesData){
        val myRef = database.getReference("Location").
        child(users.value.userId).child(item.headNote)

       var  noteData =  listLocationUser.find {
            it.userKey == users.value.userId
        }?.useValue?.find {
            it.itemKey == item.headNote
        }?.locationData

        noteData?.let {
            myRef.setValue(LocationDescriptionEntity(
                id = it.id, name = it.name,
                titleLocation = item.titleNotes, description = item.descriptionNotes,
                location = LocationEntity(it.location.longitude,it.location.latitude), time = it.time
            ))
        }
        getAllLocation()
    }

    fun deleteNoteInUser(headNote: String){
        val myRef = database.getReference("Location").
        child(users.value.userId).child(headNote)
        myRef.removeValue()
        getAllLocation()
    }
}


