package pl.tolichwer.czoperkotlin.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import pl.tolichwer.czoperkotlin.ui.NavigationActivity
import pl.tolichwer.czoperkotlin.ui.loginView.LoginActivity

@Module
abstract class ActivityBuilderModule {

    @ContributesAndroidInjector(modules = [FragmentBuildersModule::class])
    abstract fun contributeNavigationActivity(): NavigationActivity

    @ContributesAndroidInjector
    abstract fun contributeLoginActivity(): LoginActivity
}
