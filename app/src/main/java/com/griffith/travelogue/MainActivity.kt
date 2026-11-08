package com.griffith.travelogue

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.griffith.travelogue.ui.theme.TravelogueTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        // Loads the Travelogue home screen and defines
        // the navigation action for creating a new trip.
        setContent {
            TravelogueTheme {
                HomeScreen(
                    onCreateTripClick = {
                        // Opens the Create Trip screen when the user starts a new trip.
                        val intent = Intent(this,
                            CreateTripActivity::class.java)

                        startActivity(intent)
                    }
                )
            }
        }
    }
}

// Displays the main entry screen of Travelogue.
// The callback keeps navigation logic outside the UI component.
@Composable
fun HomeScreen(
    onCreateTripClick: () -> Unit
){

    // Shared colours used to maintain visual consistency across the app.
    val backgroundColor = Color(0xFFF7F8FA)
    val primaryBlue = Color(0xFF1565C0)
    val titleColor = Color(0xFF1D2939)
    val secondaryTextColor = Color(0xFF444444)

    // Main container for the Travelogue home screen.
    Box(modifier = Modifier
        .fillMaxSize()
        .background(backgroundColor)){

        // Scrollable content keeps the screen usable on different device sizes.
        Column(modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
            .padding(start = 24.dp, top = 56.dp, end = 24.dp, bottom = 32.dp)){

            // App Name
            Text(text = "TRAVELOGUE",
                color = primaryBlue,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold)

            // App Slogan
            Text(text = "Plan. Travel. Remember.",
                color = secondaryTextColor,
                fontSize = 15.sp,
                modifier = Modifier.padding(top = 4.dp))

            Spacer(modifier = Modifier.height(40.dp))

            //Section title
            Text(text ="Your next adventure",
                color = titleColor,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(16.dp))

            // Displays the initial empty state before the user creates a trip.
            Card(modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {

                Column(modifier = Modifier.fillMaxWidth().padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center){

                    Text(text = "✈",
                        fontSize = 42.sp)

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(text = "No trips yet",
                        color = titleColor,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold)

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = "Start planning your next adventure and keep your travel memories in one place.",
                        color = secondaryTextColor,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center)

                    Spacer(modifier = Modifier.height(24.dp))

                    // Starts the trip creation flow using the callback
                    // provided by MainActivity.
                    Button(onClick = onCreateTripClick,
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                        shape = RoundedCornerShape(12.dp)){

                        Text(text = "CREATE A NEW TRIP",
                            color = Color.White,
                            fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(36.dp))
        }
    }

}