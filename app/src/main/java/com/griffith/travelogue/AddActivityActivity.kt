package com.griffith.travelogue

import android.app.Activity
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.griffith.travelogue.ui.theme.TravelogueTheme

class AddActivityActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            TravelogueTheme {
                AddActivityScreen()
            }
        }
    }
}

@Composable
fun AddActivityScreen(){

    val activity = LocalActivity.current

    val backgroundColor = Color(0xFFF7F8FA)
    val primaryBlue = Color(0xFF1565C0)
    val secondaryTextColor = Color(0xFF667085)

    var activityTitle by remember { mutableStateOf("") }
    var activityDate by remember { mutableStateOf("") }
    var activityTime by remember { mutableStateOf("") }
    var activityNotes by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize()
        .fillMaxSize()
        .background(backgroundColor)
        .verticalScroll(rememberScrollState())
        .navigationBarsPadding()
        .padding(start = 24.dp, top = 56.dp, end = 24.dp, bottom = 32.dp)
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
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

            Text(text = "ADD ACTIVITY",
                color = primaryBlue,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Text(text = "Add something to your travel schedule.",
            color = secondaryTextColor,
            fontSize = 15.sp,
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(value = activityTitle,
            onValueChange = { activityTitle = it },
            label = { Text("Activity") },
            placeholder = { Text("e.g. Visit Eiffel Tower") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = activityDate,
            onValueChange = { activityDate = it },
            label = { Text("Date") },
            placeholder = { Text("DD/MM/YYYY") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = activityTime,
            onValueChange = { activityTime = it },
            label = { Text("Time") },
            placeholder = { Text("e.g. 09:00") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = activityNotes,
            onValueChange = { activityNotes = it },
            label = { Text("Notes (optional)") },
            placeholder = { Text("e.g. Tickets already booked") },
            minLines = 3,
            maxLines = 5,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(28.dp))

        Button(onClick = {

            if (
                activityTitle.isNotBlank() &&
                activityDate.isNotBlank() &&
                activityTime.isNotBlank()
            ) {

                val resultIntent = Intent()

                resultIntent.putExtra("activityTitle", activityTitle)
                resultIntent.putExtra("activityDate", activityDate)
                resultIntent.putExtra("activityTime", activityTime)
                resultIntent.putExtra("activityNotes", activityNotes)

                activity?.setResult(Activity.RESULT_OK, resultIntent)
                activity?.finish()
            }
        },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
            shape = RoundedCornerShape(12.dp)
        ){
            Text(text = "ADD TO ACTIVITY",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Activity details can be edited later.",
            color = secondaryTextColor,
            fontSize = 13.sp,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AddActivityScreenPreview(){

    TravelogueTheme{
        AddActivityScreen()
    }
}
