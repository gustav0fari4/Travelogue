package com.griffith.travelogue

import android.R.attr.onClick
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import java.time.temporal.TemporalAdjusters.next

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            TravelogueTheme {
                HomeScreen()
            }
        }
    }
}

@Composable

fun HomeScreen(){

    val backgroundColor = Color(0xFFF7F8FA)
    val primaryBlue = Color(0xFF1565C0)
    val titleColor = Color(0xFF1D2939)
    val secondaryTextColor = Color(0xFF667085)

    Box(modifier = Modifier
        .fillMaxSize()
        .background(backgroundColor)){

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

            // Empty state card
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

                    Button(onClick = {},
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