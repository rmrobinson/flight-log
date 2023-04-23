package ca.faltung.flightlog.data.database.util

import androidx.room.TypeConverter
import kotlinx.datetime.*

class InstantConverter {
    @TypeConverter
    fun longToInstant(value: Long?): Instant? =
        value?.let(Instant::fromEpochMilliseconds)

    @TypeConverter
    fun instantToLong(instant: Instant?): Long? =
        instant?.toEpochMilliseconds()
}

class LocalDateConverter {
    @TypeConverter
    fun stringToLocalDate(value: String?): LocalDate? =
        value?.toLocalDate()

    @TypeConverter
    fun localDateToString(value: LocalDate?): String? =
        value?.toString()
}

class LocalTimeConverter {
    @TypeConverter
    fun stringToLocalTime(value: String?): LocalTime? =
        value?.toLocalTime()

    @TypeConverter
    fun localTimeToString(value: LocalTime?): String? =
        value?.toString()
}
