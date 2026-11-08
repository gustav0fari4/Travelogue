package com.griffith.travelogue

import androidx.compose.runtime.mutableStateListOf

enum class ActivityStatus {
    PLANNED,
    VISITED
}

data class TravelActivity(val id: Long = System.currentTimeMillis(),
                          val tripName: String,
                          val title: String,
                          val date: String,
                          val time: String,
                          val notes: String,
                          val city: String,
                          val country: String,
                          val status: ActivityStatus = ActivityStatus.PLANNED
)

data class TravelPlace(val id: Long = System.currentTimeMillis(),
                       val tripName: String,
                       val name: String,
                       val category: String,
                       val city: String,
                       val country: String,
                       val address: String,
                       val notes: String,
                       val visited: Boolean = false,
                       val sourceActivityId: Long? = null
)

object TravelogueStore{

    val activities = mutableStateListOf<TravelActivity>()
    val places = mutableStateListOf<TravelPlace>()

    fun activitiesForTrip(tripName: String): List<TravelActivity>{
        return activities.filter{
             it.tripName == tripName
        }
    }

    fun placesForTrip(tripName: String): List<TravelPlace>{
        return places.filter{
            it.tripName == tripName
        }
    }

    fun addActivity(travelActivity: TravelActivity){
        activities.add(travelActivity)
    }

    fun addPlace(travelPlace: TravelPlace){
        places.add(travelPlace)
    }

    fun markActivityAsVisited(activityId: Long) {
        val activityIndex = activities.indexOfFirst {
            it.id == activityId
        }

        if (activityIndex == -1) {
            return
        }

        val currentActivity = activities[activityIndex]

        activities[activityIndex] = currentActivity.copy(status = ActivityStatus.VISITED)

        val alreadyAddedToPlaces = places.any { it.sourceActivityId == currentActivity.id }

        if (!alreadyAddedToPlaces) {
            places.add(
                TravelPlace(
                    tripName = currentActivity.tripName,
                    name = currentActivity.title,
                    category = "Itinerary activity",
                    city = currentActivity.city,
                    country = currentActivity.country,
                    address = "",
                    notes = currentActivity.notes,
                    visited = true,
                    sourceActivityId = currentActivity.id
                )
            )
        }
    }
}