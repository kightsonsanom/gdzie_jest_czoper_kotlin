package com.example.czoperkotlin.di

import android.app.Application
import android.content.Context
import com.example.czoperkotlin.App
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class, AndroidSupportInjectionModule::class, ActivityBuilderModule::class, ServiceBuilderModule::class])
interface AppComponent: AndroidInjector<App> {


    @Component.Factory
    interface Factory {
        fun create(@BindsInstance application: Application, @BindsInstance context: Context): AppComponent
    }

}