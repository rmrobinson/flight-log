package ca.faltung.flightlog.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ca.faltung.flightlog.data.repository.FlightRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FlightListViewModel @Inject constructor(
    private val flightRepository: FlightRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(FlightListUiState.Success(emptyList()))
    val uiState: StateFlow<FlightListUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            flightRepository.getFlights().collect { flights ->
                _uiState.value = FlightListUiState.Success(flights)
            }
        }
    }
}