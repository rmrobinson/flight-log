package ca.faltung.flightlog.data.model

import ca.faltung.flightlog.data.database.entities.FlightEntity
import kotlinx.datetime.*

/**
 * Defines a flight a person may have taken.
 */
data class Flight(
    val bookingCode: String?,
    val airlineCode: String,
    val flightNumber: Int,
    val flightDate: LocalDate,
    val origin: String,
    val destination: String,
    val scheduledDeparture: Instant?,
    val takeoff: Instant?,
    val landing: Instant?,
    val aircraft: String?,
    val aircraftRegistration: String?,
    val aircraftName: String?,
)

fun Flight.asEntity() = FlightEntity(
    airlineIataCode = airlineCode,
    flightNumber = flightNumber,
    flightDate = flightDate,
    scheduledDeparture = scheduledDeparture,
    originIataCode = origin,
    destinationIataCode = destination,
    takeoffAt = takeoff,
    landedAt = landing,
    airlineBookingCode = bookingCode,
    aircraftIataCode = aircraft,
)
