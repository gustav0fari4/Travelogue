package com.griffith.travelogue

import android.R.attr.country
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

    val backgroundColor = Color(0xFFF7F8FA)
    val primaryBlue = Color(0xFF1565C0)
    val visitedGreen = Color(0xFF2E7D32)
    val titleColor = Color(0xFF1D2939)
    val secondaryTextColor = Color(0xFF444444)

    val tripPlaces = TravelogueStore.placesForTrip(tripName)

    val addPlaceLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.let {
                data ->


                val placeName = data.getStringExtra("placeName").orEmpty()
                val placeCategory = data.getStringExtra("placeCategory").orEmpty()
                val placeCity = data.getStringExtra("placeCity").orEmpty()
                val placeAddress = data.getStringExtra("placeAddress").orEmpty()
                val placeNotes = data.getStringExtra("placeNotes").orEmpty()

                if(placeName.isNotBlank()){
                    TravelogueStore.addPlace(
                        TravelPlace(tripName = tripName,
                            name = placeName,
                            category = placeCategory,
                            city = placeCity,
                            country = country,
                            address = placeAddress,
                            notes = placeNotes,
                            visited = false
                        )
                    )
                }
            }
        }
    }

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
            text = "YOUR PLACES",
            color = titleColor,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (tripPlaces.isEmpty()) {
            // Empty places state
            EmptyPlacesCard(primaryBlue = primaryBlue,
                titleColor = titleColor,
                secondaryTextColor = secondaryTextColor,
                onAddPlace = { openAddPlaceScreen() }
            )
        } else {
            // Existing places
            tripPlaces.forEach {
                travelPlace -> PlaceCard(travelPlace = travelPlace,
                    primaryBlue = primaryBlue,
                    visitedGreen = visitedGreen,
                    titleColor = titleColor,
                    secondaryTextColor = secondaryTextColor,
                    onOpenPlace = {
                        val intent = Intent(activity, PlaceDetailsActivity::class.java).apply{
                                putExtra("placeName", travelPlace.name)
                                putExtra("placeCategory", travelPlace.category)
                                putExtra("placeCity", travelPlace.city)
                                putExtra("country", country)
                                putExtra("placeAddress", travelPlace.address)
                                putExtra("placeNotes", travelPlace.notes)
                                putExtra("placeVisited", travelPlace.visited)
                        }
                        activity?.startActivity(intent)
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
            Button(
                onClick = {
                    openAddPlaceScreen()
                },
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

@Composable
private fun EmptyPlacesCard(primaryBlue: Color,
                            titleColor: Color,
                            secondaryTextColor: Color,
                            onAddPlace: () -> Unit) {
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
                text = "No places yet",
                color = titleColor,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Add a place manually or mark an itinerary activity as visited.",
                color = secondaryTextColor,
                fontSize = 15.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onAddPlace,
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
}

@Composable
private fun PlaceCard(travelPlace: TravelPlace,
                      primaryBlue: Color,
                      visitedGreen: Color,
                      titleColor: Color,
                      secondaryTextColor: Color,
                      onOpenPlace: () -> Unit){
    Card(modifier = Modifier.fillMaxWidth().clickable { onOpenPlace() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ){
        Column(modifier = Modifier.fillMaxWidth().padding(22.dp)) {
            Text(text = travelPlace.category.uppercase(),
                color = if (travelPlace.visited){
                    visitedGreen
                } else {
                    primaryBlue
                },
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = travelPlace.name,
                color = titleColor,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(text = travelPlace.city,
                color = secondaryTextColor,
                fontSize = 14.sp
            )

            if(travelPlace.address.isNotBlank()){
                Spacer(modifier = Modifier.height(6.dp))

                Text(text = travelPlace.address,
                    color = secondaryTextColor,
                    fontSize = 14.sp
                )
            }

            if(travelPlace.notes.isNotBlank()){
                Spacer(modifier = Modifier.height(12.dp))

                Text(text = travelPlace.notes,
                    color = secondaryTextColor,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = if(travelPlace.visited){
                    "VISITED"
                } else {
                    "WANT TO VISIT"
                },
                color = if(travelPlace.visited){
                    visitedGreen
                } else {
                    primaryBlue
                },
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )

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