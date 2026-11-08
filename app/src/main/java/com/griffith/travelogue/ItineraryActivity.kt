package com.griffith.travelogue

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.griffith.travelogue.ui.theme.TravelogueTheme
import android.app.Activity
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.rememberLauncherForActivityResult

class ItineraryActivity : ComponentActivity(){

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        // Receives the current trip information from TripDashboardActivity.
        val tripName = intent.getStringExtra("tripName") ?: ""
        val city = intent.getStringExtra("city") ?: ""
        val country = intent.getStringExtra("country") ?: ""
        val startDate = intent.getStringExtra("startDate") ?: ""
        val endDate = intent.getStringExtra("endDate") ?: ""

        setContent {
            TravelogueTheme {
                ItineraryScreen(
                    tripName = tripName,
                    city = city,
                    country = country,
                    startDate = startDate,
                    endDate = endDate
                )
            }
        }
    }
}

@Composable
fun ItineraryScreen(tripName:String,
                    city:String,
                    country:String,
                    startDate:String,
                    endDate:String){

    val activity = LocalActivity.current

    val backgroundColor = Color(0xFFF7F8FA)
    val primaryBlue = Color(0xFF1565C0)
    val visitedGreen = Color(0xFF2E7D32)
    val titleColor = Color(0xFF1D2939)
    val secondaryTextColor = Color(0xFF444444)

    // Retrieves only the activities associated with the current trip.
    val tripActivities = TravelogueStore.activitiesForTrip(tripName)

    // Opens AddActivityActivity and receives the completed
    // activity data when the user returns to this screen.
    val addActivityLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->

        if (result.resultCode == Activity.RESULT_OK) {

            result.data?.let { data ->

                val title = data.getStringExtra("activityTitle").orEmpty()
                val date = data.getStringExtra("activityDate").orEmpty()
                val time = data.getStringExtra("activityTime").orEmpty()
                val notes = data.getStringExtra("activityNotes").orEmpty()

                // Creates a new TravelActivity and stores it
                // in the shared TravelogueStore.
                if(title.isNotBlank()){
                    TravelogueStore.addActivity(
                        TravelActivity(tripName = tripName,
                        title = title,
                        date = date,
                        time = time,
                        notes = notes,
                        city = city,
                        country = country)
                    )
                }
            }
        }
    }

    // Launches the Add Activity form and waits for a result.
    fun openAddActivityScreen(){
        val intent = Intent(activity, AddActivityActivity::class.java)

        addActivityLauncher.launch(intent)
    }

    // Main scrollable layout for the Itinerary screen.
    Column(modifier = Modifier.fillMaxSize()
        .background(backgroundColor)
        .verticalScroll(rememberScrollState())
        .navigationBarsPadding()
        .padding(start = 24.dp, top = 56.dp, end = 24.dp, bottom = 32.dp)
    ){

        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            TextButton(onClick = { activity?.finish() }){
                Text(text = "←",
                    color = primaryBlue,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            Text(text = "ITINERARY",
                color = primaryBlue,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Trip information
        Text(text = tripName.uppercase(),
            color = titleColor,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(text = "$startDate - $endDate",
            color = secondaryTextColor,
            fontSize = 15.sp
        )

        Spacer(modifier = Modifier.height(36.dp))

        Text(text = "YOUR SCHEDULE",
            color = titleColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Shows an empty state until the first activity is added.
        if(tripActivities.isEmpty()){
            EmptyItineraryCard(primaryBlue = primaryBlue,
                titleColor = titleColor,
                secondaryTextColor = secondaryTextColor,
                onAddActivity = { openAddActivityScreen() }
            )
        } else {
            // Displays every activity stored for the current trip.
            tripActivities.forEach{ travelActivity ->

                ActivityCard(travelActivity = travelActivity,
                    primaryBlue = primaryBlue,
                    visitedGreen = visitedGreen,
                    titleColor = titleColor,
                    secondaryTextColor = secondaryTextColor,
                    onMarkAsVisited = {
                        // Changes the activity status from PLANNED to VISITED.
                        // TravelogueStore also creates a corresponding visited Place.
                        TravelogueStore.markActivityAsVisited(travelActivity.id)
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            Button(onClick = {
                openAddActivityScreen()
            },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                shape = RoundedCornerShape(12.dp)
            ){
                Text(text = "+ ADD ACTIVITY",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// Displays the empty itinerary state and provides
// the first entry point for adding an activity.
@Composable
private fun EmptyItineraryCard(primaryBlue: Color,
                                titleColor: Color,
                                secondaryTextColor: Color,
                                onAddActivity: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(18.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "📝",
                fontSize = 40.sp
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "No activities yet",
                color = titleColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Start building your itinerary by adding activities to your trip.",
                color = secondaryTextColor,
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onAddActivity,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "+ ADD ACTIVITY",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// Displays an individual itinerary activity
// and its current PLANNED or VISITED status.
@Composable
private fun ActivityCard(travelActivity: TravelActivity,
                         primaryBlue: Color,
                         visitedGreen: Color,
                         titleColor: Color,
                         secondaryTextColor: Color,
                         onMarkAsVisited: () -> Unit) {
    // Determines the visual state and whether
    // the Mark as Visited action should still be available.
    val isVisited = travelActivity.status == ActivityStatus.VISITED

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(22.dp)) {
            Text(
                text = travelActivity.date,
                color = if (isVisited) visitedGreen else primaryBlue,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = travelActivity.time,
                color = secondaryTextColor,
                fontSize = 14.sp,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = travelActivity.title,
                color = titleColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            if (travelActivity.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = travelActivity.notes,
                    color = secondaryTextColor,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = travelActivity.status.name,
                color = if (isVisited) visitedGreen else primaryBlue,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )

            // Once visited, the activity keeps its history
            // but the action button is no longer displayed.
            if (!isVisited){
                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onMarkAsVisited,
                    modifier = Modifier.fillMaxWidth().height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = visitedGreen),
                    shape = RoundedCornerShape(12.dp)
                ){
                    Text(text = "MARK AS VISITED",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ItineraryScreenPreview(){

    TravelogueTheme{
        ItineraryScreen(
            tripName = "Japan Holiday",
            city = "Tokyo",
            country = "Japan",
            startDate = "11/11/2023",
            endDate = "26/11/2023"
        )
    }
}