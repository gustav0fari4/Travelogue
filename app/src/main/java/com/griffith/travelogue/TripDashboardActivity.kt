package com.griffith.travelogue

import android.content.Intent
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.griffith.travelogue.ui.theme.TravelogueTheme

class TripDashboardActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        // Receives the trip information created in CreateTripActivity.
        val tripName = intent.getStringExtra("tripName") ?: ""
        val country = intent.getStringExtra("country") ?: ""
        val city = intent.getStringExtra("city") ?: ""
        val startDate = intent.getStringExtra("startDate") ?: ""
        val endDate = intent.getStringExtra("endDate") ?: ""
        val notes = intent.getStringExtra("notes") ?: ""

        setContent {
            TravelogueTheme {
                TripDashboardScreen(
                    tripName = tripName,
                    country = country,
                    city = city,
                    startDate = startDate,
                    endDate = endDate,
                    notes = notes
                )
            }
        }
    }
}

// Displays the main dashboard for the current trip
// and provides navigation to Itinerary and Places.
@Composable
fun TripDashboardScreen(
    tripName: String,
    country: String,
    city: String,
    startDate: String,
    endDate: String,
    notes: String
) {
    val activity = LocalActivity.current

    // Shared colours keep the dashboard consistent
    // with the rest of the Travelogue interface.
    val backgroundColor = Color(0xFFF7F8FA)
    val primaryBlue = Color(0xFF1565C0)
    val titleColor = Color(0xFF1D2939)
    val secondaryTextColor = Color(0xFF444444)

    // Main scrollable layout for the Trip Dashboard.
    Column(modifier = Modifier.fillMaxSize()
        .background(backgroundColor)
        .verticalScroll(rememberScrollState())
        .navigationBarsPadding()
        .padding(start = 24.dp, top = 56.dp, end = 24.dp, bottom = 32.dp)) {

        // Back button and screen title
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {

            TextButton(onClick = { activity?.finish() }) {
                Text(text = "←",
                    color = primaryBlue,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            Text(text = "TRIP DASHBOARD",
                color = primaryBlue,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Displays the main information for the current trip.
        // Trip Name
        Text(text = tripName.uppercase(),
            color = titleColor,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(6.dp))

        // Destination
        Text(text = "$city, $country",
            color = secondaryTextColor,
            fontSize = 17.sp)

        Spacer(modifier = Modifier.height(4.dp))

        // Travel dates
        Text(text = "$startDate - $endDate",
            color = secondaryTextColor,
            fontSize = 15.sp)

        Spacer(modifier = Modifier.height(32.dp))

        Text(text = "YOUR TRIP",
            color = titleColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        // Provides access to the trip itinerary and planned activities.
        Card(modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)){

            Column(modifier = Modifier.padding(22.dp)){

                Text(text = "ITINERARY",
                    color = titleColor,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Plan your activities and daily schedule.",
                    color = secondaryTextColor,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(18.dp))

                Button(onClick = {

                    // Passes the current trip context to ItineraryActivity
                    // so activities remain associated with the correct trip.
                    val intent = Intent(activity, ItineraryActivity::class.java).apply{
                        putExtra("tripName", tripName)
                        putExtra("city", city)
                        putExtra("country", country)
                        putExtra("startDate", startDate)
                        putExtra("endDate", endDate)
                    }

                    activity?.startActivity(intent)
                },

                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                    shape = RoundedCornerShape(12.dp)){

                    Text(text = "OPEN ITINERARY",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Provides access to manually added and visited Places.
        Card(modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ){
            Column(modifier = Modifier.padding(22.dp)){

                Text(text = "PLACES",
                    color = titleColor,
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(text = "Keep track of the places you want to visit.",
                    color = secondaryTextColor,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(18.dp))

                Button(onClick = {
                    // Passes the trip identity and destination to PlacesActivity.
                    val intent = Intent(activity, PlacesActivity::class.java)

                    intent.putExtra("tripName", tripName)
                    intent.putExtra("city", city)
                    intent.putExtra("country", country)

                    activity?.startActivity(intent)
                },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                    shape = RoundedCornerShape(12.dp)
                ){
                    Text(text = "OPEN PLACES",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // Displays trip notes only when the user provided them.
        if (notes.isNotBlank()){

            Spacer(modifier = Modifier.height(32.dp))

            Text(text = "TRIP NOTES",
                color = titleColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Card(modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {

                Text(
                    text = notes,
                    color = secondaryTextColor,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(20.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TripDashboardScreenPreview(){

    TravelogueTheme{
        TripDashboardScreen(
            tripName = "Japan Holiday",
            country = "Japan",
            city = "Tokyo",
            startDate = "11/11/2023",
            endDate = "26/11/2023",
            notes = "First trip to Japan."
        )
    }
}
