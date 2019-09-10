package com.example.czoperkotlin.di

import com.example.czoperkotlin.di.scopes.PositionListFragmentScope
import com.example.czoperkotlin.di.scopes.SearchFragmentScope
import com.example.czoperkotlin.ui.mapView.MapFragment
import com.example.czoperkotlin.ui.positionList.PositionListFragment
import com.example.czoperkotlin.ui.searchView.SearchFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

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


}
