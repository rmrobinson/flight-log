package ca.faltung.flightlog.data.repository

import ca.faltung.flightlog.data.model.Flight
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

/**
 * Operations that can be conducted against the flight repository.
 */
interface FlightRepository {
    /**
     * Get the list of flights
     */
    fun getFlights() : Flow<List<Flight>>

    /**
     * Get a specific flight, if it exists
     */
    suspend fun getFlight(airline: String, flightNumber: Int, date: LocalDate) : Flight?

    /**
     * Get the set of most recently used booking reference codes.
     */
    fun getRecentBookingCodes() : Flow<List<String>>

    /**
     * Create a flight
     */
    suspend fun createFlight(flight: Flight)

    /**
     * Update a flight
     */
    suspend fun updateFlight(flight: Flight)

    /**
     * Delete a flight
     */
    suspend fun deleteFlight(flight: Flight)
}