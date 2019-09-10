package com.example.czoperkotlin.di

import com.example.czoperkotlin.di.scopes.LocationServiceScope
import com.example.czoperkotlin.services.LocationUpdatesService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ServiceBuilderModule{

    @LocationServiceScope
    @ContributesAndroidInjector
    abstract fun contributeLocationUpdatesService(): LocationUpdatesService

}