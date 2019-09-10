package com.example.czoperkotlin.di

import com.example.czoperkotlin.di.scopes.LocationServiceScope
import com.example.czoperkotlin.services.LocationServiceHelper
import dagger.Module
import dagger.Provides

@Module
class LocationServiceModule {

    @Provides
    @LocationServiceScope
    fun provideLocationServiceHelper(): LocationServiceHelper{
        return LocationServiceHelper()
    }

}