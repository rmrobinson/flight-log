package ca.faltung.flightlog.data.database.entities

import androidx.compose.ui.res.booleanResource
import androidx.room.ColumnInfo
import androidx.room.Entity
import ca.faltung.flightlog.data.model.Flight
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

/**
 * Defines a flight a user may take.
 */
@Entity(
    tableName = "flights",
    primaryKeys = ["airline_iata_code", "flight_number", "flight_date"],
)
data class FlightEntity(
    @ColumnInfo(name = "airline_iata_code")
    val airlineIataCode: String,
    @ColumnInfo(name = "flight_number")
    val flightNumber: Int,
    @ColumnInfo(name = "flight_date")
    val flightDate: LocalDate,
    @ColumnInfo(name = "scheduled_departure")
    val scheduledDeparture: Instant?,
    @ColumnInfo(name = "origin_iata_code")
    val originIataCode: String,
    @ColumnInfo(name = "destination_iata_code")
    val destinationIataCode: String,
    @ColumnInfo(name = "takeoff_at")
    val takeoffAt: Instant?,
    @ColumnInfo(name = "landed_at")
    val landedAt: Instant?,
    @ColumnInfo(name = "aircraft_iata_code")
    val aircraftIataCode: String?,
    @ColumnInfo(name = "airline_booking_code")
    val airlineBookingCode: String?,
)

fun FlightEntity.asExternalModel() = Flight(
    bookingCode = airlineBookingCode,
    origin = originIataCode,
    destination = destinationIataCode,
    airlineCode = airlineIataCode,
    flightNumber = flightNumber,
    flightDate = flightDate,
    scheduledDeparture = scheduledDeparture,
    takeoff = takeoffAt,
    landing = landedAt,
    aircraft = aircraftIataCode,
    aircraftName = "",
    aircraftRegistration = "",
)