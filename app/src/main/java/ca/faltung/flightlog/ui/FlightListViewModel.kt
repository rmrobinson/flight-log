package ca.faltung.flightlog.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.faltung.flightlog.data.model.Flight
import ca.faltung.flightlog.data.repository.FlightRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject
import kotlin.time.Duration

@HiltViewModel
class FlightListViewModel @Inject constructor(
    private val flightRepository: FlightRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(FlightListUiState.Success(emptyList(), emptyList()))
    val uiState: StateFlow<FlightListUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
            flightRepository.getFlights().collect { flights ->
                _uiState.value = FlightListUiState.Success(
                    upcomingFlights = flights.filter {
                        it.flightDate > today || it.flightDate == today && it.landing == null
                    },
                    pastFlights = flights.filter {
                        it.flightDate < today || it.flightDate == today && it.landing != null
                    }
                )
            }
        }
    }

    fun takeoff(flight: Flight) {
        viewModelScope.launch {
            flightRepository.updateFlight(flight.copy(
                takeoff = Clock.System.now()
            ))
        }
    }

    fun landing(flight: Flight) {
        val f = if (flight.takeoff == null) {
            flight.copy(
                takeoff = Clock.System.now().minus(Duration.parse("1s")),
                landing = Clock.System.now()
            )
        } else {
            flight.copy(
                landing = Clock.System.now()
            )
        }

        viewModelScope.launch {
            flightRepository.updateFlight(f)
        }
    }
}