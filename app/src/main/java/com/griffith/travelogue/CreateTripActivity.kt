package com.griffith.travelogue

import android.content.Intent
import androidx.compose.ui.text.input.KeyboardType
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val MINIMUM_TRIP_YEAR = 1900
private const val MAXIMUM_TRIP_YEAR = 2100

private fun formatTripDateInput(input: String): String {

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

private fun parseDate(date: String): Date? {

    if (date.length != 10) {
        return null
    }

    val year = date.takeLast(4).toIntOrNull()

    if (
        year == null ||
        year !in MINIMUM_TRIP_YEAR..MAXIMUM_TRIP_YEAR
    ) {
        return null
    }

    val formatter = SimpleDateFormat(
        "dd/MM/yyyy",
        Locale.getDefault()
    )

    formatter.isLenient = false

    return try {
        formatter.parse(date)
    } catch (exception: Exception) {
        null
    }
}

class  CreateTripActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            TravelogueTheme {
                CreateTripScreen()
            }
        }
    }
}

@Composable
fun CreateTripScreen() {

    val activity = LocalActivity.current

    val backgroundColor = Color(0xFFF7F8FA)
    val primaryBlue = Color(0xFF1565C0)
    val secondaryTextColor = Color(0xFF444444)
    val inputTextColor = Color(0xFF1D2939)
    val errorColor = Color(0xFFB3261E)

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

    var tripName by remember { mutableStateOf("") }
    var country by remember { mutableStateOf("") }
    var city by remember { mutableStateOf("") }
    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    val startDateParsed = parseDate(startDate)
    val endDateParsed = parseDate(endDate)

    val startDateIsComplete =
        startDate.length == 10

    val endDateIsComplete =
        endDate.length == 10

    val startDateIsValid =
        startDateIsComplete &&
                startDateParsed != null

    val endDateIsValid =
        endDateIsComplete &&
                endDateParsed != null

    val endDateIsBeforeStartDate =
        startDateParsed != null &&
                endDateParsed != null &&
                endDateParsed.before(startDateParsed)

    val startDateHasError =
        startDateIsComplete &&
                !startDateIsValid

    val endDateHasError =
        endDateIsComplete &&
                (
                        !endDateIsValid ||
                                endDateIsBeforeStartDate
                        )

    val dateRangeIsValid =
        startDateIsValid &&
                endDateIsValid &&
                !endDateIsBeforeStartDate

    val canCreateTrip = tripName.isNotBlank() &&
            country.isNotBlank() &&
            city.isNotBlank() &&
            dateRangeIsValid

    Column(
        modifier = Modifier.fillMaxSize()
            .background(backgroundColor)
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
            .padding(start = 24.dp, top = 56.dp, end = 24.dp, bottom = 32.dp)
    ){
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ){
            TextButton(onClick = {activity?.finish()}){
                Text(text = "←",
                    color = primaryBlue,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(4.dp))

            Text( text = "CREATE TRIP",
                color = primaryBlue,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Text(text = "Let's plan your next adventure.",
            color = secondaryTextColor,
            fontSize = 15.sp,
            modifier = Modifier.padding(top = 4.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(value = tripName,
            onValueChange = { tripName = it },
            label = { Text("Trip Name") },
            placeholder = {Text("e.g. Japan Holiday") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
            colors = textFieldColors,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = country,
            onValueChange = { country = it },
            label = { Text("Country") },
            placeholder = {Text("e.g. Japan") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
            colors = textFieldColors,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = city,
            onValueChange = { city = it },
            label = { Text("City") },
            placeholder = {Text("e.g. Tokyo") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
            colors = textFieldColors,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = startDate,
            onValueChange = { startDate = formatTripDateInput(it) },
            label = { Text("Start Date") },
            placeholder = {Text("DD/MM/YYYY") },
            supportingText = {
                if (startDateHasError) {
                    Text(
                        text = "Enter a valid date between " +
                                "$MINIMUM_TRIP_YEAR and " +
                                "$MAXIMUM_TRIP_YEAR."
                    )
                } else {
                    Text(
                        text = "Type only the numbers. " +
                                "The slashes are added automatically.",
                        color = secondaryTextColor
                    )
                }
            },
            isError = startDateHasError,            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = textFieldColors,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = endDate,
            onValueChange = { endDate = formatTripDateInput(it) },
            label = { Text("End Date") },
            placeholder = {Text("DD/MM/YYYY") },
            supportingText = {
                when {
                    endDateIsComplete && !endDateIsValid -> {
                        Text(
                            text = "Enter a valid date between " +
                                    "$MINIMUM_TRIP_YEAR and " +
                                    "$MAXIMUM_TRIP_YEAR."
                        )
                    }

                    endDateIsBeforeStartDate -> {
                        Text(
                            text = "End date cannot be before start date."
                        )
                    }

                    else -> {
                        Text(
                            text = "Type only the numbers. " +
                                    "The slashes are added automatically.",
                            color = secondaryTextColor
                        )
                    }
                }
            },
            isError = endDateHasError,            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            colors = textFieldColors,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(value = notes,
            onValueChange = { notes = it },
            label = { Text("Notes (optional)") },
            placeholder = {Text("Add anything you want to remember...") },
            minLines = 3,
            maxLines = 5,
            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
            colors = textFieldColors,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = {

                val intent = Intent(activity, TripDashboardActivity::class.java).apply {
                    putExtra("tripName", tripName.trim())
                    putExtra("country", country.trim())
                    putExtra("city", city.trim())
                    putExtra("startDate", startDate.trim())
                    putExtra("endDate", endDate.trim())
                    putExtra("notes", notes.trim()
                    )
                }
                activity?.startActivity(intent)
            },
            enabled = canCreateTrip,
            modifier = Modifier.fillMaxWidth()
                .height(52.dp),
            colors = ButtonDefaults.buttonColors(containerColor = primaryBlue),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "CREATE TRIP",
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Trip details can be edited later.",
            color = secondaryTextColor,
            fontSize = 13.sp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CreateTripScreenPreview(){

    TravelogueTheme{
        CreateTripScreen()
    }
}