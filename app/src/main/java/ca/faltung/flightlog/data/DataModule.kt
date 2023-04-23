package ca.faltung.flightlog.data

import ca.faltung.flightlog.data.repository.FlightRepository
import ca.faltung.flightlog.data.repository.OfflineFirstFlightRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {
    @Binds
    fun bindFlightRepository(
        flightRepository: OfflineFirstFlightRepository
    ): FlightRepository
}