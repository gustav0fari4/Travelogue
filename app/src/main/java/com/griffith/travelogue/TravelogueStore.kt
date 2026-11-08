package com.griffith.travelogue

import androidx.compose.runtime.mutableStateListOf

// Represents the current state of an itinerary activity.
enum class ActivityStatus {
    PLANNED,
    VISITED
}

// Stores the information required for an itinerary activity.
// New activities are PLANNED by default.
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

// Stores information about a place associated with a trip.
// sourceActivityId links a Place to an itinerary activity when
// the Place was created automatically after an activity was visited.
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

// Provides a shared in-memory data store for activities and places.
// mutableStateListOf allows Jetpack Compose screens to react to changes.
object TravelogueStore{

    val activities = mutableStateListOf<TravelActivity>()
    val places = mutableStateListOf<TravelPlace>()

    // Returns only the activities that belong to the selected trip.
    fun activitiesForTrip(tripName: String): List<TravelActivity>{
        return activities.filter{
             it.tripName == tripName
        }
    }

    // Returns only the places that belong to the selected trip.
    fun placesForTrip(tripName: String): List<TravelPlace>{
        return places.filter{
            it.tripName == tripName
        }
    }

    // Adds a new activity to the shared itinerary collection.
    fun addActivity(travelActivity: TravelActivity){
        activities.add(travelActivity)
    }

    // Adds a new place to the shared places collection.
    fun addPlace(travelPlace: TravelPlace){
        places.add(travelPlace)
    }

    // Changes an itinerary activity from PLANNED to VISITED
    // and automatically creates a corresponding visited Place.
    fun markActivityAsVisited(activityId: Long) {

        // Finds the activity using its unique ID.
        val activityIndex = activities.indexOfFirst {
            it.id == activityId
        }

        // Stops the operation if the activity cannot be found.
        if (activityIndex == -1) {
            return
        }

        val currentActivity = activities[activityIndex]

        // Updates the existing activity while preserving its other data.
        activities[activityIndex] = currentActivity.copy(status = ActivityStatus.VISITED)

        // Prevents the same activity from creating duplicate Places.
        val alreadyAddedToPlaces = places.any { it.sourceActivityId == currentActivity.id }

        if (!alreadyAddedToPlaces) {
            // Creates a visited Place using information
            // from the completed itinerary activity.
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