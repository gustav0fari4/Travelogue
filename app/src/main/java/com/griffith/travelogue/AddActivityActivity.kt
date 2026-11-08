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
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.griffith.travelogue.ui.theme.TravelogueTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Defines the acceptable year range for activity dates.
private const val MINIMUM_ACTIVITY_YEAR = 1900
private const val MAXIMUM_ACTIVITY_YEAR = 2100

// Formats numeric input into DD/MM/YYYY.
// Example: 12112026 becomes 12/11/2026.
private fun formatActivityDateInput(input: String): String {

    val digits = input
        .filter { it.isDigit() }
        .take(8)

    return when {
        digits.length <= 2 -> digits

        digits.length <= 4 -> {
            "${digits.take(2)}/${digits.drop(2)}"
        }

        else -> {
            "${digits.take(2)}/" +
                    "${digits.substring(2, 4)}/" +
                    digits.drop(4)
        }
    }
}

// Validates that the activity date is a real calendar date
// and that the year is within the accepted range.
private fun parseActivityDate(date: String): Date? {

    if (date.length != 10) {
        return null
    }

    val year = date
        .takeLast(4)
        .toIntOrNull()

    if (
        year == null ||
        year !in MINIMUM_ACTIVITY_YEAR..MAXIMUM_ACTIVITY_YEAR
    ) {
        return null
    }

    val formatter = SimpleDateFormat(
        "dd/MM/yyyy",
        Locale.getDefault()
    )

    // Prevents invalid dates such as 31/11/2026
    // from being automatically corrected by the formatter.
    formatter.isLenient = false

    return try {
        formatter.parse(date)
    } catch (exception: Exception) {
        null
    }
}

// Formats numeric input into HH:mm.
// Example: 0930 becomes 09:30.
private fun formatActivityTimeInput(input: String): String {

    val digits = input
        .filter { it.isDigit() }
        .take(4)

    return when {
        digits.length <= 2 -> digits

        else -> {
            "${digits.take(2)}:${digits.drop(2)}"
        }
    }
}

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

    // App colours used to keep the form consistent with the other screens.
    val backgroundColor = Color(0xFFF7F8FA)
    val primaryBlue = Color(0xFF1565C0)
    val secondaryTextColor = Color(0xFF444444)
    val inputTextColor = Color(0xFF1D2939)
    val errorColor = Color(0xFFB3261E)

    // Shared styling for all input fields on this screen.
    // Darker input text improves readability on physical devices.
    val textFieldColors = OutlinedTextFieldDefaults.colors(
        focusedTextColor = inputTextColor,
        unfocusedTextColor = inputTextColor,

        focusedBorderColor = primaryBlue,
        unfocusedBorderColor = Color(0xFF7A7A7A),

        focusedLabelColor = primaryBlue,
        unfocusedLabelColor = secondaryTextColor,

        focusedPlaceholderColor = Color(0xFF777777),
        unfocusedPlaceholderColor = Color(0xFF777777),

        cursorColor = primaryBlue,

        errorTextColor = inputTextColor,
        errorBorderColor = errorColor,
        errorCursorColor = errorColor,
        errorLabelColor = errorColor,
        errorSupportingTextColor = errorColor
    )

    // Stores the values entered by the user in the activity form.
    var activityTitle by remember { mutableStateOf("") }
    var activityDate by remember { mutableStateOf("") }
    var activityTime by remember { mutableStateOf("") }
    var activityNotes by remember { mutableStateOf("") }

    // Validates the activity date before allowing the activity to be saved.
    val activityDateIsComplete =
        activityDate.length == 10

    val activityDateParsed =
        parseActivityDate(activityDate)

    val activityDateIsValid =
        activityDateIsComplete &&
                activityDateParsed != null

    val activityDateHasError =
        activityDateIsComplete &&
                !activityDateIsValid

    val activityTimeIsComplete =
        activityTime.length == 5

    // The activity can only be added when all required fields are complete
    // and the date has passed calendar validation.
    val canAddActivity =
        activityTitle.isNotBlank() &&
                activityDateIsValid &&
                activityTimeIsComplete

    // Main scrollable layout for the Add Activity screen.
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
            // Returns to the Itinerary screen without creating an activity.
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
            placeholder = { Text("e.g. Visit Tokyo Tower") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
            colors = textFieldColors,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Date input automatically adds "/" and displays an error
        // when a complete but invalid date is entered.
        OutlinedTextField(value = activityDate,
            onValueChange = { newValue -> activityDate = formatActivityDateInput(newValue) },
            label = { Text("Date") },
            placeholder = { Text("DD/MM/YYYY") },
            supportingText = {
                if (activityDateHasError) {
                    Text(
                        text = "Enter a valid date between " +
                                "$MINIMUM_ACTIVITY_YEAR and " +
                                "$MAXIMUM_ACTIVITY_YEAR."
                    )
                } else {
                    Text(
                        text = "Type only the numbers. " +
                                "The slashes are added automatically.",
                        color = secondaryTextColor
                    )
                }
            },
            isError = activityDateHasError,
            singleLine = true,
            colors = textFieldColors,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Time input uses a numeric keyboard and automatically adds ":".
        OutlinedTextField(value = activityTime,
            onValueChange = { newValue -> activityTime = formatActivityTimeInput(newValue) },
            label = { Text("Time") },
            placeholder = { Text("e.g. 09:00") },
            supportingText = {
                Text(
                    text = "Type only the numbers. " +
                            "The colon is added automatically.",
                    color = secondaryTextColor
                )
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = textFieldColors,
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
            colors = textFieldColors,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Returns the completed activity data to ItineraryActivity.
        Button(onClick = {
            // Packages the activity data so ItineraryActivity can receive it.
            val resultIntent = Intent().apply {
                    putExtra("activityTitle", activityTitle.trim())
                    putExtra("activityDate", activityDate)
                    putExtra("activityTime", activityTime)
                    putExtra("activityNotes", activityNotes.trim())
                }
            // Sends the activity back to the previous screen and closes this screen.
            activity?.setResult(Activity.RESULT_OK, resultIntent)
                activity?.finish()
            },
            enabled = canAddActivity,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
            shape = RoundedCornerShape(12.dp)
        ){
            Text(text = "ADD TO ITINERARY",
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
