package ca.faltung.flightlog.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import ca.faltung.flightlog.data.database.entities.FlightEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

/**
 * Data access object for flights stored in the Room database.
 */
@Dao
interface FlightDao {
    /**
     * Get the list of flights, sorted by most recent scheduled departure.
     */
    @Transaction
    @Query(
        value = """
            SELECT * FROM flights
            ORDER BY flight_date DESC
    """
    )
    fun getFlights(): Flow<List<FlightEntity>>

    /**
     * Retrieve a specific flight, if it exists
     */
    @Transaction
    @Query(
        value = """
            SELECT * FROM flights
            WHERE airline_iata_code=:airline AND flight_number=:flightNumber AND flight_date=:date
        """
    )
    suspend fun getFlight(airline: String, flightNumber: Int, date: LocalDate): FlightEntity?

    /**
     * Get the list of 5 most recently used booking codes
     */
    @Transaction
    @Query(
        value = """
            SELECT DISTINCT airline_booking_code FROM flights
            ORDER BY flight_date DESC
            LIMIT 5
    """
    )
    fun getRecentBookingCodes(): Flow<List<String>>

    /**
     * Insert a new flight into the database.
     */
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertFlight(entity: FlightEntity)

    /**
     * Update an existing flight into the database.
     */
    @Update
    suspend fun updateFlight(entity: FlightEntity)

    /**
     * Delete an existing flight in the database.
     */
    @Delete
    suspend fun deleteFlight(entity: FlightEntity)
}