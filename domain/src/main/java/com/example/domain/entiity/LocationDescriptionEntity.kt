package com.example.domain.entiity

data class LocationDescriptionEntity(
    var id: String = "",
    var name: String = "",
    var titleLocation: String = "",
    var description: String = "",
    var location: LocationEntity = LocationEntity(),
    var time: String = ""
)