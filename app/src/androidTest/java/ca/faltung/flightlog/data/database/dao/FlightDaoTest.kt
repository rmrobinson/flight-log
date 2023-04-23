package ca.faltung.flightlog.data.database.dao

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import ca.faltung.flightlog.data.database.FlightDatabase
import ca.faltung.flightlog.data.database.entities.FlightEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toLocalDate
import kotlinx.datetime.toLocalTime
import org.junit.Before
import org.junit.Test

class FlightDaoTest {
    private lateinit var flightDao: FlightDao
    private lateinit var db: FlightDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, FlightDatabase::class.java).build()
        flightDao = db.flightDao()
    }

    @Test
    fun testCreateFlightAndRetrieve() {
        val newFlight = FlightEntity(
            airlineIataCode = "AC",
            flightNumber = 123,
            scheduledDepartureDate = "2023-01-01".toLocalDate(),
            scheduledDepartureTime = "11:23:45".toLocalTime(),
            originIataCode = "YYZ",
            destinationIataCode = "YYC",
            arrivedAt = null,
            departedAt = null,
            aircraftIataCode = null,
            airlineBookingCode = null,
        )

        runTest {
            flightDao.insertFlight(newFlight)

            val foundFlights = flightDao.getFlights().first()

            assert(foundFlights.size == 1)
            assert(foundFlights[0].flightNumber == 123)
            assert(foundFlights[0].airlineIataCode == "AC")
            assert(foundFlights[0].scheduledDepartureDate.toString() == "2023-01-01")
            assert(foundFlights[0].scheduledDepartureTime.toString() == "11:23:45")
        }
    }

    @Test
    fun testRecentBookingCodes() {

        runTest {
            for (i in 1..10) {
                val outboundFlight = FlightEntity(
                    airlineIataCode = "AC",
                    flightNumber = i,
                    scheduledDepartureDate = LocalDate(2022, i, 1),
                    scheduledDepartureTime = "11:23:45".toLocalTime(),
                    originIataCode = "YYZ",
                    destinationIataCode = "YYC",
                    arrivedAt = null,
                    departedAt = null,
                    aircraftIataCode = null,
                    airlineBookingCode = "TEST$i",
                )
                val inboundFlight = FlightEntity(
                    airlineIataCode = "AC",
                    flightNumber = i,
                    scheduledDepartureDate = LocalDate(2022, i, 2),
                    scheduledDepartureTime = "12:23:45".toLocalTime(),
                    originIataCode = "YYC",
                    destinationIataCode = "YYZ",
                    arrivedAt = null,
                    departedAt = null,
                    aircraftIataCode = null,
                    airlineBookingCode = "TEST$i",
                )

                // Make sure there are doubles to capture realistic behaviour.
                flightDao.insertFlight(outboundFlight)
                flightDao.insertFlight(inboundFlight)
            }

            val foundFlights = flightDao.getRecentBookingCodes().first()

            assert(foundFlights.size == 5)
            assert(foundFlights[0] == "TEST10")
            assert(foundFlights[4] == "TEST6")
        }
    }
}