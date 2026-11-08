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
import androidx.compose.material3.OutlinedTextFieldDefaults
import com.griffith.travelogue.ui.theme.TravelogueTheme

class AddPlaceActivity : ComponentActivity(){

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        // Receives the trip city so the form can be pre-filled for the user.
        val defaultCity = intent.getStringExtra("city") ?: ""

        setContent {
            TravelogueTheme {
                AddPlaceScreen(defaultCity = defaultCity)
            }
        }
    }
}

@Composable
fun AddPlaceScreen(defaultCity: String){

    val activity = LocalActivity.current

    // App colours used to keep the form consistent with the other screens.
    val backgroundColor = Color(0xFFF7F8FA)
    val primaryBlue = Color(0xFF1565C0)
    val secondaryTextColor = Color(0xFF444444)
    val inputTextColor = Color(0xFF1D2939)

    // Shared input styling improves readability and keeps all form fields consistent.
    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = inputTextColor,
        unfocusedTextColor = inputTextColor,

        focusedBorderColor = primaryBlue,
        unfocusedBorderColor = Color(0xFF7A7A7A),

        focusedLabelColor = primaryBlue,
        unfocusedLabelColor = secondaryTextColor,

        focusedPlaceholderColor = Color(0xFF777777),
        unfocusedPlaceholderColor = Color(0xFF777777),

        cursorColor = primaryBlue
    )

    // Stores the values entered by the user in the Add Place form.
    var placeName by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("") }
    var city by remember { mutableStateOf(defaultCity) }
    var address by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    // Main scrollable layout for the Add Place screen.
    Column(modifier = Modifier.fillMaxSize()
        .background(backgroundColor)
        .verticalScroll(rememberScrollState())
        .navigationBarsPadding()
        .padding(start = 24.dp, top = 56.dp, end = 24.dp, bottom = 32.dp)
    ){
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            // Returns to the Places screen without saving a new place.
            TextButton(onClick = { activity?.finish() }){
                Text(text = "←",
                    color = primaryBlue,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            Text(text = "ADD PLACE",
                color = primaryBlue,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Text(text = "Save somewhere you want to visit.",
            color = secondaryTextColor,
            fontSize = 15.sp,
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(value = placeName,
            onValueChange = { placeName = it },
            label = { Text("Place Name") },
            placeholder = { Text("e.g. Tokyo Tower") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
            colors = textFieldColors,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = category,
            onValueChange = { category = it },
            label = { Text("Category") },
            placeholder = { Text("e.g. Attraction") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
            colors = textFieldColors,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // The city is pre-filled from the current trip but can still be edited.
        OutlinedTextField(value = city,
            onValueChange = { city = it },
            label = { Text("City") },
            placeholder = { Text("e.g. Tokyo") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
            colors = textFieldColors,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = address,
            onValueChange = { address = it },
            label = { Text("Address (optional)") },
            placeholder = { Text("e.g. 4 Chome-2-4 Shibakoen") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
            colors = textFieldColors,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = notes,
            onValueChange = { notes = it },
            label = { Text("Notes (optional)") },
            placeholder = { Text("e.g. Visit before sunset") },
            minLines = 3,
            maxLines = 5,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
            colors = textFieldColors,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Packages the new place data and returns it to PlacesActivity.
        Button(onClick = {
            if(placeName.isNotBlank() &&
                category.isNotBlank() &&
                city.isNotBlank()
            ){

                val resultIntent = Intent()

                resultIntent.putExtra("placeName", placeName)
                resultIntent.putExtra("placeCategory", category)
                resultIntent.putExtra("placeCity", city)
                resultIntent.putExtra("placeAddress", address)
                resultIntent.putExtra("placeNotes", notes)

                // Sends the place back to the previous screen and closes this screen.
                activity?.setResult(Activity.RESULT_OK, resultIntent)
                activity?.finish()
            }
        },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
            shape = RoundedCornerShape(12.dp)
        ){
            Text(text = "ADD TO PLACES",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Place details can be edited later.",
            color = secondaryTextColor,
            fontSize = 13.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun AddPlaceScreenPreview(){
    TravelogueTheme{
        AddPlaceScreen(defaultCity = "Tokyo")
    }
}