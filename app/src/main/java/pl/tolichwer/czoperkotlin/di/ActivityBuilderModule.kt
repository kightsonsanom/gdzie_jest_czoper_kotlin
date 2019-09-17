package pl.tolichwer.czoperkotlin.di

import pl.tolichwer.czoperkotlin.ui.NavigationActivity
import pl.tolichwer.czoperkotlin.ui.loginView.LoginActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeNavigationActivity(): NavigationActivity

    @ContributesAndroidInjector
    abstract fun contributeLoginActivity(): LoginActivity

}
