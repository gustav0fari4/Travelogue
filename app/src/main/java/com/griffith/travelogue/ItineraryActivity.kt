package com.griffith.travelogue

import android.R.attr.text
import android.app.ProgressDialog.show
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
import androidx.compose.material3.CardDefaults.shape
import androidx.compose.material3.CheckboxDefaults.colors
import androidx.compose.material3.FloatingActionButtonDefaults.elevation
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

class ItineraryActivity : ComponentActivity(){

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        val tripName = intent.getStringExtra("tripName") ?: ""
        val startDate = intent.getStringExtra("startDate") ?: ""
        val endDate = intent.getStringExtra("endDate") ?: ""

        setContent {
            TravelogueTheme {
                ItineraryScreen(
                    tripName = tripName,
                    startDate = startDate,
                    endDate = endDate
                )
            }
        }
    }
}

@Composable
fun ItineraryScreen(tripName:String,
                    startDate:String,
                    endDate:String){

    val activity = LocalActivity.current

    var activityTitle by remember { mutableStateOf("") }
    var activityDate by remember { mutableStateOf("") }
    var activityTime by remember { mutableStateOf("") }
    var activityNotes by remember { mutableStateOf("") }

    val addActivityLauncher = rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult()) { result ->

        if (result.resultCode == Activity.RESULT_OK) {

            result.data?.let { data ->

                activityTitle = data.getStringExtra("activityTitle") ?: ""
                activityDate = data.getStringExtra("activityDate") ?: ""
                activityTime = data.getStringExtra("activityTime") ?: ""
                activityNotes = data.getStringExtra("activityNotes") ?: ""
            }
        }
    }

    val backgroundColor = Color(0xFFF7F8FA)
    val primaryBlue = Color(0xFF1565C0)
    val titleColor = Color(0xFF1D2939)
    val secondaryTextColor = Color(0xFF667085)

    fun openAddActivityScreen(){
        val intent = Intent(activity, AddActivityActivity::class.java)

        addActivityLauncher.launch(intent)
    }

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


        if(activityTitle.isBlank()){
            // Empty itinerary
            Card(modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ){
                Column(modifier = Modifier.fillMaxWidth().padding(28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Text(text = "📅",
                        fontSize = 40.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(text = "No activities yet",
                        color = titleColor,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = "Starting building your itinerary by adding activities to your trip.",
                        color = secondaryTextColor,
                        fontSize = 15.sp,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(onClick = {
                        openAddActivityScreen()
                    },
                        modifier = Modifier.fillMaxWidth().height(52.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "ADD ACTIVITY",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        } else {
            // Created activity
            Card(modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ){
                Column(modifier = Modifier.fillMaxWidth().padding(22.dp),){
                    Text(text = activityDate,
                        color = primaryBlue,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(text = activityTime,
                        color = secondaryTextColor,
                        fontSize = 14.sp,
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(text = activityTitle,
                        color = titleColor,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    if(activityNotes.isNotBlank()){
                        Spacer(modifier = Modifier.height(4.dp))

                        Text(text = activityNotes,
                            color = secondaryTextColor,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(text= "PLANNED",
                        color = primaryBlue,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

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

@Preview(showBackground = true)
@Composable
fun ItineraryScreenPreview(){

    TravelogueTheme{
        ItineraryScreen(
            tripName = "Japan Dream",
            startDate = "11/11/2023",
            endDate = "26/11/2023"
        )
    }
}