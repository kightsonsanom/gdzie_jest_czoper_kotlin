package pl.tolichwer.czoperkotlin.di

import dagger.Module
import dagger.android.ContributesAndroidInjector
import pl.tolichwer.czoperkotlin.di.scopes.PositionListFragmentScope
import pl.tolichwer.czoperkotlin.di.scopes.SearchFragmentScope
import pl.tolichwer.czoperkotlin.di.scopes.UserListFragmentScope
import pl.tolichwer.czoperkotlin.ui.mapView.MapFragment
import pl.tolichwer.czoperkotlin.ui.positionList.PositionListFragment
import pl.tolichwer.czoperkotlin.ui.positionList.UserListFragment
import pl.tolichwer.czoperkotlin.ui.searchView.SearchFragment

@Module
abstract class FragmentBuildersModule {

    @ContributesAndroidInjector
    abstract fun contributeMapFragment(): MapFragment

    @SearchFragmentScope
    @ContributesAndroidInjector
    abstract fun contributeSearchFragment(): SearchFragment

    @PositionListFragmentScope
    @ContributesAndroidInjector
    abstract fun contributePositionListFragment(): PositionListFragment

    @UserListFragmentScope
    @ContributesAndroidInjector
    abstract fun contributeUserListFragment(): UserListFragment


}
