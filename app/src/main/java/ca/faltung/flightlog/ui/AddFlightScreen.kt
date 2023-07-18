package ca.faltung.flightlog.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ca.faltung.flightlog.R
import ca.faltung.flightlog.ui.theme.FlightLogTheme
import kotlinx.datetime.*

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
internal fun AddFlightRoute(
    modifier: Modifier = Modifier,
    onFlightAdded: () -> Unit,
    viewModel: AddFlightViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val flightAdded by viewModel.flightAdded.collectAsStateWithLifecycle()

    if (flightAdded) {
        LaunchedEffect(Unit) {
            onFlightAdded()
        }
    }

    when (uiState) {
        is AddFlightUiState.Success -> AddFlightScreen(
            modifier = modifier,
            uiState = uiState as AddFlightUiState.Success,
            bookingCodeChanged = viewModel::onBookingCodeEntered,
            airlineCodeChanged = viewModel::onAirlineCodeEntered,
            flightNumberChanged = viewModel::onFlightNumberEntered,
            flightDateChanged = viewModel::onFlightDateEntered,
            flightTimeChanged = viewModel::onFlightTimeEntered,
            departureAirportChanged = viewModel::onDepartureAirportEntered,
            arrivalAirportChanged = viewModel::onArrivalAirportEntered,
            airplaneModelChanged = viewModel::onAirplaneModelEntered,
            airplaneRegistrationChanged = viewModel::onAirplaneRegistrationEntered,
            createFlight = { viewModel.createFlight() }
        )
        is AddFlightUiState.Error -> Text(text = "Error adding flight: " + (uiState as AddFlightUiState.Error).exception.toString())
    }
}

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
internal fun AddFlightScreen (
    modifier: Modifier = Modifier,
    uiState: AddFlightUiState.Success,
    bookingCodeChanged: (String) -> Unit = {},
    airlineCodeChanged: (String) -> Unit = {},
    flightNumberChanged: (String) -> Unit = {},
    flightDateChanged: (String) -> Unit = {},
    flightTimeChanged: (String) -> Unit = {},
    departureAirportChanged: (String) -> Unit = {},
    arrivalAirportChanged: (String) -> Unit = {},
    airplaneModelChanged: (String) -> Unit = {},
    airplaneRegistrationChanged: (String) -> Unit = {},
    createFlight: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.Start
    ) {
        ValidatedTextField(
            modifier = modifier,
            label = { Text("Booking Reference") },
            singleLine = true,
            input = StringInput(
                value = uiState.bookingCode,
                errorResId = null,
            ),
            onValueChange = bookingCodeChanged,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
            )
        )

        val airlineCode = StringInput(
            value = uiState.airlineCode,
            errorResId = if (uiState.airlineCodeValid != true) R.string.add_flight_screen_airline_code_invalid else null
        )
        ValidatedTextField(
            modifier = modifier,
            label = { Text("Airline Code")},
            singleLine = true,
            input = airlineCode,
            onValueChange = airlineCodeChanged,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
            )
        )

        // TODO: more accurate error info here
        val flightNumber = StringInput(
            value = uiState.flightNumber,
            errorResId = if (uiState.flightNumberValid != true) R.string.add_flight_screen_flight_number_not_numeric else null
        )
        ValidatedTextField(
            modifier = modifier,
            label = { Text("Flight Number")},
            singleLine = true,
            input = flightNumber,
            onValueChange = flightNumberChanged,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next,
            )
        )

        DatePicker(
            label = { Text("Flight Date") },
            value = uiState.flightDate,
            onValueChange = flightDateChanged,
            modifier = modifier,
        )

        TimePicker(
            label = { Text("Scheduled Departure Time") },
            value = uiState.flightTime,
            onValueChange = flightTimeChanged,
            modifier = modifier
        )

        val departureAirport = StringInput(
            value = uiState.departureAirportCode,
            errorResId = if (uiState.departureAirportCodeValid != true) R.string.add_flight_screen_departure_airport_invalid else null
        )
        ValidatedTextField(
            modifier = modifier,
            label = { Text("Departing Airport Code")},
            singleLine = true,
            input = departureAirport,
            onValueChange = departureAirportChanged,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
            )
        )

        val arrivalAirport = StringInput(
            value = uiState.arrivalAirportCode,
            errorResId = if (uiState.arrivalAirportCodeValid != true) R.string.add_flight_screen_arrival_airport_invalid else null
        )
        ValidatedTextField(
            modifier = modifier,
            label = { Text("Arriving Airport Code")},
            singleLine = true,
            input = arrivalAirport,
            onValueChange = arrivalAirportChanged,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
            )
        )

        val airplaneModel = StringInput(
            value = uiState.airplaneModel,
            errorResId = null
        )
        ValidatedTextField(
            modifier = modifier,
            label = { Text("Airplane IATA Code")},
            singleLine = true,
            input = airplaneModel,
            onValueChange = airplaneModelChanged,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
            )
        )

        val airplaneRegistration = StringInput(
            value = uiState.airplaneRegistration,
            errorResId = null
        )
        ValidatedTextField(
            modifier = modifier,
            label = { Text("Airplane Registration")},
            singleLine = true,
            input = airplaneRegistration,
            onValueChange = airplaneRegistrationChanged,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
            )
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Button(
            enabled = uiState.airlineCodeValid == true &&
                    uiState.flightNumberValid == true &&
                    uiState.flightDateValid == true &&
                    uiState.flightTimeValid == true &&
                    uiState.departureAirportCodeValid == true &&
                    uiState.arrivalAirportCodeValid == true &&
                    !uiState.flightAlreadyExists,
            onClick = createFlight) {
            Text(text = "Create Flight")
        }
        if (uiState.flightAlreadyExists) {
            Text(text = stringResource(id = R.string.add_flight_screen_flight_already_exists))
        }
    }
}

@Preview
@Composable
fun PreviewAddFlightScreen() {
    FlightLogTheme {
        AddFlightScreen(uiState = AddFlightUiState.Success(
            airlineCode = "AC",
            airlineCodeValid = true,
            flightNumber = "123",
            flightNumberValid = true,
            flightDate = "2023-01-01",
            flightDateValid = true,
            flightTime = "00:00:01",
            flightTimeValid = true,
            departureAirportCode = "YYZ",
            departureAirportCodeValid = true,
            arrivalAirportCode = "YYC",
            arrivalAirportCodeValid = true,
            airplaneModel = "B738",
            airplaneRegistration = "Y-ABCD"
        ))
    }
}

@Preview
@Composable
fun PreviewAddFlightScreenFlightExists() {
    FlightLogTheme {
        AddFlightScreen(uiState = AddFlightUiState.Success(
            airlineCode = "AC",
            flightNumber = "123",
            flightDate = "2023-01-01",
            flightTime = "00:00:01",
            departureAirportCode = "YYZ",
            arrivalAirportCode = "YYZ",
            airplaneModel = "B738",
            airplaneRegistration = "Y-ABCD",
            flightAlreadyExists = true,
        ))
    }
}
