package ca.faltung.flightlog.data.database

import ca.faltung.flightlog.data.database.dao.FlightDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DaosModule {
    @Provides
    fun providesFlightDao(
        database: FlightDatabase,
    ): FlightDao = database.flightDao()
}