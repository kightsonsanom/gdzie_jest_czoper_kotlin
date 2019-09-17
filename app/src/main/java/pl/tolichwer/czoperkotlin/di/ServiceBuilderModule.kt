package pl.tolichwer.czoperkotlin.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import pl.tolichwer.czoperkotlin.services.LocationUpdatesService

@Module
abstract class ServiceBuilderModule{

    @ContributesAndroidInjector
    abstract fun contributeLocationUpdatesService(): LocationUpdatesService

}