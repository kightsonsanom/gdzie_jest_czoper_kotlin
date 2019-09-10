package com.example.czoperkotlin.di

import com.example.czoperkotlin.ui.NavigationActivity
import com.example.czoperkotlin.ui.loginView.LoginActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeNavigationActivity(): NavigationActivity

    @ContributesAndroidInjector
    abstract fun contributeLoginActivity(): LoginActivity

}
