package ca.faltung.flightlog.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ca.faltung.flightlog.data.model.Flight
import ca.faltung.flightlog.ui.theme.FlightLogTheme
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDate

@OptIn(ExperimentalLifecycleComposeApi::class)
@Composable
internal fun FlightListRoute(
    modifier: Modifier = Modifier,
    viewModel: FlightListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (uiState) {
        is FlightListUiState.Success -> FlightListScreen(
            modifier = modifier,
            uiState = uiState as FlightListUiState.Success,
            takeoff = viewModel::takeoff,
            landing = viewModel::landing,
        )
        is FlightListUiState.Error -> Text(text = "Error retrieving flights: " + (uiState as FlightListUiState.Error).exception.toString())
    }
}

@Composable
internal fun FlightListScreen(
    modifier: Modifier = Modifier,
    uiState: FlightListUiState.Success,
    takeoff: (Flight) -> Unit = {},
    landing: (Flight) -> Unit = {},
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items(
            items = uiState.flights,
            key = { flight ->
                flight.airlineCode + flight.flightNumber + flight.scheduledDeparture.toString()
            }
        ) { flight ->
            FlightCard(flight = flight, onArrivalClicked = { if (flight.landing == null) { landing(flight) } }, onDepartureClicked = { if (flight.takeoff == null) { takeoff(flight) } })
            Divider(color = Color.Black)
        }
    }
}

@Preview
@Composable
fun PreviewFlightListScreen() {
    FlightLogTheme {
        FlightListScreen(
            uiState = FlightListUiState.Success(previewFlightList)
        )
    }
}

val previewFlightList = listOf(
    Flight(
        origin ="YYC",
        destination = "YYZ",
        airlineCode = "AC",
        flightNumber = 1237,
        flightDate = "2022-06-30".toLocalDate(),
        takeoff = "2022-06-30T14:33:00Z".toInstant(),
        landing = "2022-06-30T17:57:00Z".toInstant(),
        scheduledDeparture = "2022-06-30T14:30:00Z".toInstant(),
        aircraft = null,
        aircraftRegistration = "",
        aircraftName = "",
        bookingCode = "TEST123",
    ),
    Flight(
        origin ="YYZ",
        destination = "YYC",
        airlineCode = "AC",
        flightNumber = 1234,
        flightDate = "2022-06-21".toLocalDate(),
        takeoff = "2022-06-21T07:11:00Z".toInstant(),
        landing = "2022-06-21T11:46:00Z".toInstant(),
        scheduledDeparture = "2022-06-21T07:00:00Z".toInstant(),
        aircraft = "B738",
        aircraftRegistration = "",
        aircraftName = "",
        bookingCode = "TEST123",
    )
)
