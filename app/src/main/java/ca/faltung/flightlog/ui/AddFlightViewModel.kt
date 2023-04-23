package ca.faltung.flightlog.ui

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.faltung.flightlog.R
import ca.faltung.flightlog.data.model.Flight
import ca.faltung.flightlog.data.repository.FlightRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import javax.inject.Inject

const val add_flight_vm_booking_code = "add_flight_vm_booking_code"
const val add_flight_vm_airline_code = "add_flight_vm_airline_code"
const val add_flight_vm_flight_number = "add_flight_vm_flight_number"
const val add_flight_vm_flight_date = "add_flight_vm_flight_date"
const val add_flight_vm_flight_time = "add_flight_vm_flight_time"
const val add_flight_vm_departure_airport = "add_flight_vm_departure_airport"
const val add_flight_vm_arrival_airport = "add_flight_vm_arrival_airport"
const val add_flight_vm_airplane_model = "add_flight_vm_airplane_model"
const val add_flight_vm_airplane_registration = "add_flight_vm_airplane_registration"
const val add_flight_vm_validate_err = "add_flight_vm_validate_err"
const val add_flight_vm_status = "add_flight_vm_status"

@HiltViewModel
class AddFlightViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val flightRepository: FlightRepository
) : ViewModel() {
    val bookingCode = savedStateHandle.getStateFlow(add_flight_vm_booking_code, StringInput())
    val airlineCode = savedStateHandle.getStateFlow(add_flight_vm_airline_code, StringInput())
    val flightNumber = savedStateHandle.getStateFlow(add_flight_vm_flight_number, StringInput())
    val flightDate = savedStateHandle.getStateFlow(add_flight_vm_flight_date, StringInput())
    val flightTime = savedStateHandle.getStateFlow(add_flight_vm_flight_time, StringInput())
    val departureAirport =
        savedStateHandle.getStateFlow(add_flight_vm_departure_airport, StringInput())
    val arrivalAirport = savedStateHandle.getStateFlow(add_flight_vm_arrival_airport, StringInput())
    val airplaneModel = savedStateHandle.getStateFlow(add_flight_vm_airplane_model, StringInput())
    val airplaneRegistration =
        savedStateHandle.getStateFlow(add_flight_vm_airplane_registration, StringInput())
    val validateErr = savedStateHandle.getStateFlow(add_flight_vm_validate_err, 0)
    val status = savedStateHandle.getStateFlow(add_flight_vm_status, false)

    private val isFirstFlightInfoValid =
        combine(
            bookingCode,
            airlineCode,
            flightNumber,
            flightDate,
            flightTime
        ) { bc, ac, fn, fd, ft ->
            bc.value.isNotBlank() && bc.errorResId == null &&
                    ac.value.isNotBlank() && ac.errorResId == null &&
                    fn.value.isNotBlank() && fn.errorResId == null &&
                    fd.value.isNotBlank() && fd.errorResId == null &&
                    ft.value.isNotBlank() && ft.errorResId == null
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), false)

    val isValid =
        combine(
            isFirstFlightInfoValid,
            departureAirport,
            arrivalAirport,
            airplaneModel,
            validateErr
        ) { ffi, da, aa, am, ve ->
            ffi &&
                    da.value.isNotBlank() && da.errorResId == null &&
                    aa.value.isNotBlank() && aa.errorResId == null &&
                    am.value.isNotBlank() && am.errorResId == null &&
                    ve == 0
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), false)

    fun onBookingCodeEntered(code: String) {
        savedStateHandle[add_flight_vm_booking_code] = bookingCode.value.copy(
            value = code.uppercase(),
            errorResId = null
        )
    }

    fun onAirlineCodeEntered(code: String) {
        var errId: Int? = null
        if (!code.isNullOrEmpty() && code.length != 2) {
            errId = R.string.add_flight_screen_airline_code_invalid
        }

        savedStateHandle[add_flight_vm_airline_code] = airlineCode.value.copy(
            value = code.uppercase(),
            errorResId = errId
        )

        refreshValidateState()
    }

    fun onFlightNumberEntered(code: String) {
        var errId: Int? = null
        if (!code.isNullOrEmpty() && !code.isDigitsOnly()) {
            errId = R.string.add_flight_screen_flight_number_not_numeric
        } else if (!code.isNullOrEmpty() && code.length > 5) {
            errId = R.string.add_flight_screen_flight_number_too_long
        }

        savedStateHandle[add_flight_vm_flight_number] = flightNumber.value.copy(
            value = code,
            errorResId = errId,
        )

        refreshValidateState()
    }

    fun onFlightDateEntered(date: String) {
        var errId: Int? = null
        try {
            LocalDate.parse(date)
        } catch (e: java.lang.Exception) {
            errId = R.string.add_flight_screen_flight_date_invalid
        }

        savedStateHandle[add_flight_vm_flight_date] = flightDate.value.copy(
            value = date,
            errorResId = errId,
        )

        refreshValidateState()
    }

    fun onFlightTimeEntered(t: String) {
        var errId: Int? = null
        try {
            LocalTime.parse(t)
        } catch (e: java.lang.Exception) {
            errId = R.string.add_flight_screen_flight_time_invalid
        }

        savedStateHandle[add_flight_vm_flight_time] = flightTime.value.copy(
            value = t,
            errorResId = errId,
        )
    }

    fun onDepartureAirportEntered(code: String) {
        var errId: Int? = null
        if (!isValidAirportCode(code)) {
            errId = R.string.add_flight_screen_departure_airport_invalid
        }

        savedStateHandle[add_flight_vm_departure_airport] = departureAirport.value.copy(
            value = code.uppercase(),
            errorResId = errId,
        )
    }

    fun onArrivalAirportEntered(code: String) {
        var errId: Int? = null
        if (!isValidAirportCode(code)) {
            errId = R.string.add_flight_screen_arrival_airport_invalid
        } else if (departureAirport.value.value == code) {
            errId = R.string.add_flight_screen_departure_arrival_airports_the_same
        }

        savedStateHandle[add_flight_vm_arrival_airport] = arrivalAirport.value.copy(
            value = code.uppercase(),
            errorResId = errId,
        )
    }

    fun onAirplaneModelEntered(model: String) {
        savedStateHandle[add_flight_vm_airplane_model] = airplaneModel.value.copy(
            value = model.uppercase(),
            errorResId = null,
        )
    }

    fun onAirplaneRegistrationEntered(reg: String) {
        savedStateHandle[add_flight_vm_airplane_registration] = airplaneRegistration.value.copy(
            value = reg.uppercase(),
            errorResId = null,
        )
    }

    private fun refreshValidateState() {
        var ld : LocalDate
        try {
            ld = LocalDate.parse(flightDate.value.value)
        } catch (e: java.lang.Exception) {
            return
        }

        viewModelScope.launch {
            val f = flightRepository.getFlight(airlineCode.value.value, flightNumber.value.value.toInt(), ld)
            if (f != null) {
                savedStateHandle[add_flight_vm_validate_err] = R.string.add_flight_screen_flight_already_exists
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
            val scheduledDepartureTime =
                if (flightTime.value.value.isNotBlank()) LocalTime.parse(flightTime.value.value) else LocalTime.parse(
                    "00:00:00"
                )
            val scheduledDepartureDate = LocalDate.parse(flightDate.value.value)
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
                bookingCode = bookingCode.value.value,
                airlineCode = airlineCode.value.value,
                flightNumber = flightNumber.value.value.toInt(),
                flightDate = flightDate.value.value.toLocalDate(),
                scheduledDeparture = scheduledDeparture.toInstant(TimeZone.currentSystemDefault()),
                destination = arrivalAirport.value.value,
                origin = departureAirport.value.value,
                aircraft = airplaneModel.value.value,
                aircraftRegistration = airplaneRegistration.value.value,
                aircraftName = "",
                takeoff = null,
                landing = null,
            )

            try {
                flightRepository.createFlight(flight)
            } catch (e: Exception) {
                savedStateHandle[add_flight_vm_status] = e.message
            }
        }
    }
}