package ca.faltung.flightlog.ui

import ca.faltung.flightlog.data.model.Flight

sealed class FlightListUiState {
    data class Success(
        val upcomingFlights: List<Flight>,
        val pastFlights: List<Flight>
    ): FlightListUiState()
    data class Error(val exception: Throwable): FlightListUiState()
}