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
import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.clickable
import com.griffith.travelogue.ui.theme.TravelogueTheme

class PlacesActivity : ComponentActivity(){

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val tripName = intent.getStringExtra("tripName") ?: ""
        val city = intent.getStringExtra("city") ?: ""
        val country = intent.getStringExtra("country") ?: ""

        setContent {
            TravelogueTheme {
                PlacesScreen(
                    tripName = tripName,
                    city = city,
                    country = country
                )
            }
        }
    }
}

@Composable
fun PlacesScreen(tripName:String,
                city: String,
                country: String) {

    val activity = LocalActivity.current

    var placeName by remember { mutableStateOf("") }
    var placeCategory by remember { mutableStateOf("") }
    var placeCity by remember { mutableStateOf("") }
    var placeAddress by remember { mutableStateOf("") }
    var placeNotes by remember { mutableStateOf("") }

    val addPlaceLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let { data ->

                    placeName = data.getStringExtra("placeName") ?: ""
                    placeCategory = data.getStringExtra("placeCategory") ?: ""
                    placeCity = data.getStringExtra("placeCity") ?: ""
                    placeAddress = data.getStringExtra("placeAddress") ?: ""
                    placeNotes = data.getStringExtra("placeNotes") ?: ""
                }
            }
        }

    val backgroundColor = Color(0xFFF7F8FA)
    val primaryBlue = Color(0xFF1565C0)
    val titleColor = Color(0xFF1D2939)
    val secondaryTextColor = Color(0xFF667085)

    fun openAddPlaceScreen() {
        val intent = Intent(activity, AddPlaceActivity::class.java)

        intent.putExtra("city", city)

        addPlaceLauncher.launch(intent)
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .background(backgroundColor)
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
            .padding(start = 24.dp, top = 56.dp, end = 24.dp, bottom = 32.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
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
                text = "PLACES",
                color = primaryBlue,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        //Trip information
        Text(
            text = tripName.uppercase(),
            color = titleColor,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "$city, $country",
            color = secondaryTextColor,
            fontSize = 15.sp
        )

        Spacer(modifier = Modifier.height(36.dp))

        Text(
            text = "PLACES TO VISIT",
            color = titleColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (placeName.isBlank()) {
            // Empty places state
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🗺️",
                        fontSize = 40.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "No places yet",
                        color = titleColor,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Save the places you want to visit in your trip.",
                        color = secondaryTextColor,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            openAddPlaceScreen()
                        },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "ADD PLACE",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        } else {
            // Created place
            Card(
                modifier = Modifier.fillMaxWidth()
                                    .clickable{
                                        val intent = Intent(activity, PlaceDetailsActivity::class.java)
                                            intent.putExtra("placeName", placeName)
                                            intent.putExtra("placeCategory", placeCategory)
                                            intent.putExtra("placeCity", placeCity)
                                            intent.putExtra("country", country)
                                            intent.putExtra("placeAddress", placeAddress)
                                            intent.putExtra("placeNotes", placeNotes)

                                            activity?.startActivity(intent)
                                    },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.fillMaxWidth().padding(22.dp)) {
                    Text(
                        text = placeCategory.uppercase(),
                        color = primaryBlue,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = placeName,
                        color = titleColor,
                        fontSize = 21.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = placeCity,
                        color = secondaryTextColor,
                        fontSize = 14.sp
                    )

                    if (placeAddress.isNotBlank()) {
                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = placeAddress,
                            color = secondaryTextColor,
                            fontSize = 14.sp
                        )
                    }

                    if (placeNotes.isNotBlank()) {
                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = placeNotes,
                            color = secondaryTextColor,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "WANT TO VISIT",
                        color = primaryBlue,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = { openAddPlaceScreen() },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = "+ ADD PLACE",
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PlacesScreenPreview(){

    TravelogueTheme{
        PlacesScreen(
            tripName = "Japan Dream",
            city = "Tokyo",
            country = "Japan"
        )
    }
}