package ca.faltung.flightlog.ui

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.faltung.flightlog.data.model.Flight
import ca.faltung.flightlog.data.repository.FlightRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import javax.inject.Inject

private const val add_flight_vm_state = "add_flight_vm_state"


/**
 * ViewModel for the AddFlight screen
 */
@HiltViewModel
class AddFlightViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val flightRepository: FlightRepository
) : ViewModel() {
    val uiState: StateFlow<AddFlightUiState> = savedStateHandle.getStateFlow(add_flight_vm_state, AddFlightUiState.Success())
    private val _flightAdded = MutableStateFlow(false)
    val flightAdded: StateFlow<Boolean> = _flightAdded.asStateFlow()

    fun onBookingCodeEntered(code: String) {
        val oldUiState = uiState.value as AddFlightUiState.Success
        savedStateHandle[add_flight_vm_state] = oldUiState.copy(
            bookingCode = code.uppercase(),
        )
    }

    fun onAirlineCodeEntered(code: String) {
        var isValid = false
        if (!code.isNullOrEmpty() && code.length == 2) {
            isValid = true
        }

        val oldUiState = uiState.value as AddFlightUiState.Success
        savedStateHandle[add_flight_vm_state] = oldUiState.copy(
            airlineCode = code.uppercase(),
            airlineCodeValid = isValid,
        )

        refreshValidateState()
    }

    fun onFlightNumberEntered(code: String) {
        var isValid = false
        if (!code.isNullOrEmpty() && code.isDigitsOnly() && code.length < 6) {
            isValid = true
        }

        val oldUiState = uiState.value as AddFlightUiState.Success
        savedStateHandle[add_flight_vm_state] = oldUiState.copy(
            flightNumber = code,
            flightNumberValid = isValid
        )

        refreshValidateState()
    }

    fun onFlightDateEntered(date: String) {
        var isValid = true
        try {
            LocalDate.parse(date)
        } catch (e: java.lang.Exception) {
            isValid = false
        }

        val oldUiState = uiState.value as AddFlightUiState.Success
        savedStateHandle[add_flight_vm_state] = oldUiState.copy(
            flightDate = date,
            flightDateValid = isValid,
        )

        refreshValidateState()
    }

    fun onFlightTimeEntered(t: String) {
        var isValid = true
        try {
            LocalTime.parse(t)
        } catch (e: java.lang.Exception) {
            isValid = false
        }

        val oldUiState = uiState.value as AddFlightUiState.Success
        savedStateHandle[add_flight_vm_state] = oldUiState.copy(
            flightTime = t,
            flightTimeValid = isValid,
        )
    }

    fun onDepartureAirportEntered(code: String) {
        var isValid = isValidAirportCode(code)

        val oldUiState = uiState.value as AddFlightUiState.Success
        savedStateHandle[add_flight_vm_state] = oldUiState.copy(
            departureAirportCode = code.uppercase(),
            departureAirportCodeValid = isValid,
        )
    }

    fun onArrivalAirportEntered(code: String) {
        val oldUiState = uiState.value as AddFlightUiState.Success
        var isValid = isValidAirportCode(code) && oldUiState.departureAirportCode != code.uppercase()

        savedStateHandle[add_flight_vm_state] = oldUiState.copy(
            arrivalAirportCode = code.uppercase(),
            arrivalAirportCodeValid = isValid,
        )
    }

    fun onAirplaneModelEntered(model: String) {
        val oldUiState = uiState.value as AddFlightUiState.Success
        savedStateHandle[add_flight_vm_state] = oldUiState.copy(
            airplaneModel = model.uppercase(),
        )
    }

    fun onAirplaneRegistrationEntered(reg: String) {
        val oldUiState = uiState.value as AddFlightUiState.Success
        savedStateHandle[add_flight_vm_state] = oldUiState.copy(
            airplaneRegistration = reg.uppercase(),
        )
    }

    private fun refreshValidateState() {
        val oldUiState = uiState.value as AddFlightUiState.Success
        var ld : LocalDate
        try {
            ld = LocalDate.parse(oldUiState.flightDate)
        } catch (e: java.lang.Exception) {
            return
        }

        viewModelScope.launch {
            val f = flightRepository.getFlight(oldUiState.airlineCode, oldUiState.flightNumber.toInt(), ld)
            if (f != null) {
                savedStateHandle[add_flight_vm_state] = oldUiState.copy(
                    flightAlreadyExists = true
                )
            }
        }
    }

    private fun isValidAirportCode(code: String): Boolean {
        if (code.length != 3) {
            return false
        }
        for (c in code) {
            if (c in 'a'..'z' || c in 'A'..'Z') {
                continue
            }

            return false
        }

        return true
    }

    fun createFlight() {
        viewModelScope.launch {
            val currUiState = uiState.value as AddFlightUiState.Success

            val scheduledDepartureTime =
                if (currUiState.flightTime.isNotBlank()) LocalTime.parse(currUiState.flightTime) else LocalTime.parse(
                    "00:00:00"
                )
            val scheduledDepartureDate = LocalDate.parse(currUiState.flightDate)
            val scheduledDeparture = LocalDateTime.parse(
                "%d-%02d-%02dT%02d:%02d:00".format(
                    scheduledDepartureDate.year,
                    scheduledDepartureDate.monthNumber,
                    scheduledDepartureDate.dayOfMonth,
                    scheduledDepartureTime.hour,
                    scheduledDepartureTime.minute
                )
            )
            val flight = Flight(
                bookingCode = currUiState.bookingCode,
                airlineCode = currUiState.airlineCode,
                flightNumber = currUiState.flightNumber.toInt(),
                flightDate = currUiState.flightDate.toLocalDate(),
                scheduledDeparture = scheduledDeparture.toInstant(TimeZone.currentSystemDefault()),
                destination = currUiState.arrivalAirportCode,
                origin = currUiState.departureAirportCode,
                aircraft = currUiState.airplaneModel,
                aircraftRegistration = currUiState.airplaneRegistration,
                aircraftName = "",
                takeoff = null,
                landing = null,
            )

            try {
                flightRepository.createFlight(flight)

                _flightAdded.value = true
            } catch (e: Exception) {
                savedStateHandle[add_flight_vm_state] = AddFlightUiState.Error(e)
            }
        }
    }
}