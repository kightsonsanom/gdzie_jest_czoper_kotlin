package com.example.czoperkotlin

import com.example.czoperkotlin.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import dagger.android.support.DaggerApplication
import javax.inject.Inject

class App: DaggerApplication(), HasAndroidInjector{

    @Inject lateinit var androidInjector : DispatchingAndroidInjector<Any>

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {

        return DaggerAppComponent.factory().create(this, applicationContext)

    }

    override fun androidInjector(): AndroidInjector<Any> = androidInjector

}