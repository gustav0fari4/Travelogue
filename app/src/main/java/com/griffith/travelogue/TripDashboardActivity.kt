package com.griffith.travelogue

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.material3.CardElevation
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
import androidx.core.view.WindowCompat.enableEdgeToEdge
import com.griffith.travelogue.ui.theme.TravelogueTheme

class TripDashboardActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

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

    val backgroundColor = Color(0xFFF7F8FA)
    val primaryBlue = Color(0xFF1565C0)
    val titleColor = Color(0xFF1D2939)
    val secondaryTextColor = Color(0xFF667085)

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

        Text(text = notes,
            color = secondaryTextColor,
            fontSize = 15.sp)

        // Itinerary card
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
                    // Itinerary navigation will be added next.
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

        // Places card
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
                // Places navigation will be added later
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
            tripName = "Japan Dream",
            country = "Japan",
            city = "Tokyo",
            startDate = "11/11/2023",
            endDate = "26/11/2023",
            notes = "First trip to Japan."
        )
    }
}
