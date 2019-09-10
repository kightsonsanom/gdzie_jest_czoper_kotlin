package com.example.czoperkotlin.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.czoperkotlin.ui.loginView.LoginActivityViewModel
import com.example.czoperkotlin.ui.mapView.MapFragmentViewModel
import com.example.czoperkotlin.ui.positionList.PositionListFragmentViewModel
import com.example.czoperkotlin.ui.searchView.SearchFragmentViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MapFragmentViewModel::class)
    abstract fun bindMapViewModel(mapFragmentViewModel: MapFragmentViewModel): ViewModel


    @Binds
    @IntoMap
    @ViewModelKey(PositionListFragmentViewModel::class)
    abstract fun bindPositionListFragmentViewModel(positionListFragmentViewModel: PositionListFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchFragmentViewModel::class)
    abstract fun bindSearchFragmentViewModel(searchFragmentViewModel: SearchFragmentViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LoginActivityViewModel::class)
    abstract fun bindLoginActivityViewModel(loginActivityViewModel: LoginActivityViewModel): ViewModel


    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

}