package ca.faltung.flightlog.data.repository

import android.util.Log
import ca.faltung.flightlog.data.database.dao.FlightDao
import ca.faltung.flightlog.data.database.entities.FlightEntity
import ca.faltung.flightlog.data.database.entities.asExternalModel
import ca.faltung.flightlog.data.model.Flight
import ca.faltung.flightlog.data.model.asEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import javax.inject.Inject

/**
 * Offline-first implementation of the flight repository
 */
class OfflineFirstFlightRepository @Inject constructor (
    private val flightDao: FlightDao
) : FlightRepository {
    override fun getFlights(): Flow<List<Flight>> =
        flightDao.getFlights().map { it.map(FlightEntity::asExternalModel) }

    override suspend fun getFlight(airline: String, flightNumber: Int, date: LocalDate): Flight? {
        val f = flightDao.getFlight(airline, flightNumber, date)
        if (f != null) {
            Log.d("flight_dao", "got flight " + airline + flightNumber.toString() + " on " + date.toString())
            Log.d("flight_da", "found flight " + f.airlineIataCode + f.flightNumber.toString() + " from booking " + f.airlineBookingCode)
            return f.asExternalModel()
        }
        return null
    }

    override fun getRecentBookingCodes(): Flow<List<String>> =
        flightDao.getRecentBookingCodes()

    override suspend fun createFlight(flight: Flight) {
        flightDao.insertFlight(flight.asEntity())
    }

    override suspend fun updateFlight(flight: Flight) {
        flightDao.updateFlight(flight.asEntity())
    }

    override suspend fun deleteFlight(flight: Flight) {
        flightDao.deleteFlight(flight.asEntity())
    }
}