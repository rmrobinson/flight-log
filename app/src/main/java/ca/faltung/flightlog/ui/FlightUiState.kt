package ca.faltung.flightlog.ui

import kotlinx.datetime.Instant

data class FlightUiState(
    val airlineCode: String,
    val flightNumber: Int,
    val scheduledDeparture: Instant,
    val origin: String,
    val destination: String,
    val takeoff: Instant?,
    val landing: Instant?,
    val aircraft: String?,
    val aircraftRegistration: String?,
    val aircraftName: String?,
)