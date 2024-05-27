package com.example.data.models

import com.example.domain.entiity.LocationDescriptionEntity

data class LocationUser(
    var userKey: String =  "",
    var useValue: List<LocationUserItem> = listOf()
)

data class LocationUserItem(
    var itemKey: String = "",
    var locationData: LocationData = LocationData()
)

data class LocationData(
    var id: String = "",
    var name: String = "",
    var titleLocation: String = "",
    var descriptionLocation: String = "",
    var location: Location  = Location(),
    var time: String = ""
)

data class Location(
    var longitude: Double = 0.0,
    var latitude: Double = 0.0
)