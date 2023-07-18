package ca.faltung.flightlog.ui

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class AddFlightUiState {
    @Parcelize
    data class Success (
        val bookingCode: String = "",
        val airlineCode: String = "",
        val airlineCodeValid: Boolean? = null,
        val flightNumber: String = "",
        val flightNumberValid: Boolean? = null,
        val flightDate: String = "",
        val flightDateValid: Boolean? = null,
        val flightTime: String = "",
        val flightTimeValid: Boolean? = null,
        val departureAirportCode: String = "",
        val departureAirportCodeValid: Boolean? = null,
        val arrivalAirportCode: String = "",
        val arrivalAirportCodeValid: Boolean? = null,
        val airplaneModel: String = "",
        val airplaneModelValid: Boolean? = null,
        val airplaneRegistration: String = "",
        val flightAlreadyExists: Boolean = false,
    ) : Parcelable, AddFlightUiState()

    data class Error(val exception: Throwable): AddFlightUiState()
}