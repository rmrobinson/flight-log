package ca.faltung.flightlog.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ca.faltung.flightlog.data.database.dao.FlightDao
import ca.faltung.flightlog.data.database.entities.FlightEntity
import ca.faltung.flightlog.data.database.util.InstantConverter
import ca.faltung.flightlog.data.database.util.LocalDateConverter
import ca.faltung.flightlog.data.database.util.LocalTimeConverter

@Database(entities = [FlightEntity::class], version = 1)
@TypeConverters(
    InstantConverter::class,
    LocalDateConverter::class,
    LocalTimeConverter::class,
)
abstract class FlightDatabase : RoomDatabase() {
    abstract fun flightDao(): FlightDao
}