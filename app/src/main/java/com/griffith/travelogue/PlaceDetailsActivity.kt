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

class PlaceDetailsActivity : ComponentActivity(){

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        // Receives the selected place information from PlacesActivity.
        val placeName = intent.getStringExtra("placeName") ?: ""
        val placeCategory = intent.getStringExtra("placeCategory") ?: ""
        val placeCity = intent.getStringExtra("placeCity") ?: ""
        val country = intent.getStringExtra("country") ?: ""
        val placeAddress = intent.getStringExtra("placeAddress") ?: ""
        val placeNotes = intent.getStringExtra("placeNotes") ?: ""
        // Determines whether the place has already been visited
        // or is still marked as Want to Visit.
        val placeVisited = intent.getBooleanExtra("placeVisited", false)

        setContent {
            TravelogueTheme {
                PlaceDetailsScreen(
                    placeName = placeName,
                    placeCategory = placeCategory,
                    placeCity = placeCity,
                    country = country,
                    placeAddress = placeAddress,
                    placeNotes = placeNotes,
                    placeVisited = placeVisited
                )
            }
        }
    }
}

// Displays the complete information for a selected place,
// including its status, optional address and notes.
@Composable
fun PlaceDetailsScreen(placeName: String,
                      placeCategory: String,
                      placeCity: String,
                      country: String,
                      placeAddress: String,
                      placeNotes: String,
                       placeVisited: Boolean
) {

    val activity = LocalActivity.current

    // Shared colours keep the Place Details screen
    // visually consistent with the rest of Travelogue.
    val backgroundColor = Color(0xFFF7F8FA)
    val primaryBlue = Color(0xFF1565C0)
    val titleColor = Color(0xFF1D2939)
    val secondaryTextColor = Color(0xFF444444)

    // Main scrollable layout for the Place Details screen.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
            .padding(start = 24.dp, top = 56.dp, end = 24.dp, bottom = 32.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Returns to the Places screen.
            TextButton(onClick = { activity?.finish() }) {
                Text(
                    text = "←",
                    color = primaryBlue,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            Text(
                text = "PLACE DETAILS",
                color = primaryBlue,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Displays the main place information and location.
        Text(
            text = placeName,
            color = titleColor,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = placeCategory,
            color = primaryBlue,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "$placeCity, $country",
            color = secondaryTextColor,
            fontSize = 16.sp
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Displays the place status together with optional
        // address and notes when these values are available.
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier
                .fillMaxWidth()
                .padding(22.dp)) {
                Text(
                    text = "STATUS",
                    color = secondaryTextColor,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Uses the stored visit status to distinguish between
                // completed visits and places that are still planned.
                Text(
                    text = if (placeVisited) {
                        "VISITED"
                    } else {
                        "WANT TO VISIT"
                    },
                    color = if (placeVisited) {
                        Color(0xFF2E7D32)
                    } else {
                        primaryBlue
                    },
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )

                // Optional information is only displayed when it was provided.
                if (placeAddress.isNotBlank()) {

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "ADDRESS",
                        color = secondaryTextColor,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = placeAddress,
                        color = titleColor,
                        fontSize = 16.sp,
                    )
                }

                if (placeNotes.isNotBlank()) {

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "NOTES",
                        color = secondaryTextColor,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = placeNotes,
                        color = titleColor,
                        fontSize = 16.sp,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Shares the selected place using Android's implicit intent system.
        Button(
            onClick = {

                // Builds a simple text summary containing the place information.
                val shareText = """
                    $placeName
                    $placeCategory in $placeCity, $country

                    Saved to my Travelogue trip.
                """.trimIndent()

                // ACTION_SEND allows any compatible installed application
                // to receive the place information as plain text.
                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, shareText)
                }

                // Opens Android's native sharing interface so the user
                // can choose which application to share the place with.
                val chooserIntent = Intent.createChooser(sendIntent, "Share place")

                activity?.startActivity(chooserIntent)

            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "SHARE PLACE",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Communicates a planned feature without exposing
        // functionality that is not implemented in this milestone.
        Text(
            text = "Location check-in will be available in a future version.",
            color = secondaryTextColor,
            fontSize = 13.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PlaceDetailsScreenPreview() {

    TravelogueTheme {
        PlaceDetailsScreen(
            placeName = "Tokyo Tower",
            placeCategory = "Attraction",
            placeCity = "Tokyo",
            country = "Japan",
            placeAddress = "4 Chome-2-4 Shibakoen",
            placeNotes = "Visit before sunset",
            placeVisited = false
        )
    }
}