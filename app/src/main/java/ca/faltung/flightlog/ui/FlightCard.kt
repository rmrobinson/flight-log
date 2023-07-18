package ca.faltung.flightlog.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ca.faltung.flightlog.data.model.Flight
import ca.faltung.flightlog.R
import ca.faltung.flightlog.ui.theme.FlightLogTheme
import kotlinx.datetime.*
import java.time.format.DateTimeFormatter

@Composable
fun FlightCard(
    flight: Flight,
    onDepartureClicked: (Int) -> Unit = {},
    onArrivalClicked: (Int) -> Unit = {}
) {
    Row (
        modifier = Modifier.padding(all = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column (
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = scheduledDateFormatted(scheduledDate = flight.flightDate))
            Text(text = "${flight.airlineCode}${flight.flightNumber}",
                color = MaterialTheme.colors.secondaryVariant)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            flightEvent(airportCode = flight.destination, flightTime = flight.landing, iconResId = R.drawable.ic_flight_land_line, iconDescResId = R.string.flight_card_arrival_icon_content_desc, onClick = onArrivalClicked)
            Spacer(modifier = Modifier.height(4.dp))
            flightEvent(airportCode = flight.origin, flightTime = flight.takeoff, iconResId = R.drawable.ic_flight_takeoff_line, iconDescResId = R.string.flight_card_departure_icon_content_desc, onClick = onDepartureClicked)
        }
    }
}

@Composable
private fun flightEvent(airportCode: String, flightTime: Instant?, iconResId: Int, iconDescResId: Int, onClick: (Int) -> Unit) {
    Row (
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = airportCode)
        Spacer(modifier = Modifier.width(8.dp))
        Image(
            painterResource(id = iconResId),
            contentDescription = stringResource(iconDescResId),
        )
        Spacer(modifier = Modifier.width(8.dp))
        ClickableText(
            text = AnnotatedString(
                text = if (flightTime != null) flightTimeFormatted(flightTime = flightTime)
                        else stringResource(R.string.flight_card_time_pending)
            ),
            onClick = onClick,
        )
    }
}

@Composable
fun scheduledDateFormatted(scheduledDate: LocalDate): String {
    return DateTimeFormatter.ofPattern("yyyy-MM-dd").format(scheduledDate.toJavaLocalDate())
}

@Composable
fun flightTimeFormatted(flightTime: Instant): String {
    return DateTimeFormatter.ofPattern("HH:mm:ss").format(flightTime.toLocalDateTime(TimeZone.currentSystemDefault()).toJavaLocalDateTime())
}

@Preview(name = "Light Mode")
@Composable
fun PreviewFlightCard() {
    FlightLogTheme {
        FlightCard(
            previewFlightList[0]
        )
    }
}
