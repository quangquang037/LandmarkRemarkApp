package com.example.data.mapper

import android.util.Log
import com.example.data.models.Location
import com.example.data.models.LocationData
import com.example.data.models.LocationUser
import com.example.data.models.LocationUserItem
import com.google.firebase.database.DataSnapshot

class LocationMapper {

    companion object{
        fun mapper(data: Iterable<DataSnapshot>): MutableList<LocationUser> {
            val listItem: MutableList<LocationUser> = mutableListOf()
            data.forEach {
                listItem.add(
                    LocationUser().apply {
                        userKey = it.key ?: ""
                        useValue = mapperLocationUserItem(it.value as Map<String, Any>)
                    }
                )
            }
            return listItem
        }

        private fun mapperLocationUserItem(map: Map<String, Any>): List<LocationUserItem> {
            val listItem: MutableList<LocationUserItem> = mutableListOf()
            map.forEach {
                listItem.add(
                    LocationUserItem().apply {
                        itemKey = it.key
                        locationData = mapperLocationData(it.value as Map<String, Any>)
                    }
                )
            }
            return listItem
        }

        private fun mapperLocationData(map: Map<String,Any>): LocationData{
            return LocationData().apply {
                map.forEach {
                    val values = it.value.toString()
                    when(it.key){
                        "id" -> id = values
                        "name" -> name = values
                        "titleLocation" -> titleLocation = values
                        "description" -> descriptionLocation = values
                        "location" -> location = mapperLocation(it.value as HashMap<String,Any>)
                        "time" -> time = values
                    }
                }
            }
        }

        private fun mapperLocation(map: HashMap<String,Any>): Location {
            return Location().apply {
                map.forEach {
                    when(it.key){
                        "longitude" -> longitude = it.value as Double
                        "latitude" -> latitude = it.value as Double
                    }
                }
            }
        }
    }
}