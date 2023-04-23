package ca.faltung.flightlog.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import ca.faltung.flightlog.ui.theme.FlightLogTheme
import kotlinx.datetime.*

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
internal fun AddFlightRoute(
    modifier: Modifier = Modifier,
    viewModel: AddFlightViewModel = hiltViewModel(),
) {
    AddFlightScreen(
        modifier = modifier,
        viewModel = viewModel,
    )
}


@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
internal fun AddFlightScreen (
    modifier: Modifier = Modifier,
    viewModel: AddFlightViewModel,
) {
    val bookingCode by viewModel.bookingCode.collectAsStateWithLifecycle()
    val airlineCode by viewModel.airlineCode.collectAsStateWithLifecycle()
    val flightNumber by viewModel.flightNumber.collectAsStateWithLifecycle()
    val flightDate by viewModel.flightDate.collectAsStateWithLifecycle()
    val flightTime by viewModel.flightTime.collectAsStateWithLifecycle()
    val departureAirport by viewModel.departureAirport.collectAsStateWithLifecycle()
    val arrivalAirport by viewModel.arrivalAirport.collectAsStateWithLifecycle()
    val airplaneModel by viewModel.airplaneModel.collectAsStateWithLifecycle()
    val airplaneRegistration by viewModel.airplaneRegistration.collectAsStateWithLifecycle()

    val inputsValid by viewModel.isValid.collectAsStateWithLifecycle()
    val validateErr by viewModel.validateErr.collectAsStateWithLifecycle()

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
            input = bookingCode,
            onValueChange = viewModel::onBookingCodeEntered,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
            )
        )
        ValidatedTextField(
            modifier = modifier,
            label = { Text("Airline Code")},
            singleLine = true,
            input = airlineCode,
            onValueChange = viewModel::onAirlineCodeEntered,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
            )
        )
        ValidatedTextField(
            modifier = modifier,
            label = { Text("Flight Number")},
            singleLine = true,
            input = flightNumber,
            onValueChange = viewModel::onFlightNumberEntered,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next,
            )
        )
        DatePicker(
            label = { Text("Flight Date") },
            value = flightDate.value,
            onValueChange = viewModel::onFlightDateEntered,
            modifier = modifier,
        )
        TimePicker(
            label = { Text("Scheduled Departure Time") },
            value = flightTime.value,
            onValueChange = viewModel::onFlightTimeEntered,
            modifier = modifier
        )
        ValidatedTextField(
            modifier = modifier,
            label = { Text("Departing Airport Code")},
            singleLine = true,
            input = departureAirport,
            onValueChange = viewModel::onDepartureAirportEntered,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
            )
        )
        ValidatedTextField(
            modifier = modifier,
            label = { Text("Arriving Airport Code")},
            singleLine = true,
            input = arrivalAirport,
            onValueChange = viewModel::onArrivalAirportEntered,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
            )
        )
        ValidatedTextField(
            modifier = modifier,
            label = { Text("Airplane IATA Code")},
            singleLine = true,
            input = airplaneModel,
            onValueChange = viewModel::onAirplaneModelEntered,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
            )
        )
        ValidatedTextField(
            modifier = modifier,
            label = { Text("Airplane Registration")},
            singleLine = true,
            input = airplaneRegistration,
            onValueChange = viewModel::onAirplaneRegistrationEntered,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next,
            )
        )
        Spacer(modifier = Modifier.padding(8.dp))
        Button(
            enabled = inputsValid && validateErr == 0,
            onClick = { viewModel.createFlight() }) {
            Text(text = "Create Flight")
        }
        if (validateErr > 0) {
            Text(text = stringResource(id = validateErr))
        }
    }
}

@Preview
@Composable
fun PreviewAddFlightScreen() {
    FlightLogTheme {
        //AddFlightScreen()
    }
}
