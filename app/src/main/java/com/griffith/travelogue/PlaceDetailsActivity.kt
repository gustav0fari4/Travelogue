package com.griffith.travelogue

import android.R.attr.fontWeight
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

        val placeName = intent.getStringExtra("placeName") ?: ""
        val placeCategory = intent.getStringExtra("placeCategory") ?: ""
        val placeCity = intent.getStringExtra("placeCity") ?: ""
        val country = intent.getStringExtra("country") ?: ""
        val placeAddress = intent.getStringExtra("placeAddress") ?: ""
        val placeNotes = intent.getStringExtra("placeNotes") ?: ""

        setContent {
            TravelogueTheme {
                PlaceDetailsScreen(
                    placeName = placeName,
                    placeCategory = placeCategory,
                    placeCity = placeCity,
                    country = country,
                    placeAddress = placeAddress,
                    placeNotes = placeNotes
                )
            }
        }
    }
}

@Composable
fun PlaceDetailsScreen(placeName: String,
                      placeCategory: String,
                      placeCity: String,
                      country: String,
                      placeAddress: String,
                      placeNotes: String) {

    val activity = LocalActivity.current

    val backgroundColor = Color(0xFFF7F8FA)
    val primaryBlue = Color(0xFF1565C0)
    val titleColor = Color(0xFF1D2939)
    val secondaryTextColor = Color(0xFF667085)

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

        // Place title
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

                Text(
                    text = "Want to Visit",
                    color = primaryBlue,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )

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

        Button(
            onClick = {

                val shareText = """
                    $placeName
                    $placeCategory in $placeCity, $country

                    Saved to my Travelogue trip.
                """.trimIndent()

                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, shareText)
                }

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
            placeNotes = "Visit before sunset"
        )
    }
}