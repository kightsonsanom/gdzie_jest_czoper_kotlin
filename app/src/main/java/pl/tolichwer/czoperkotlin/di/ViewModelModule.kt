package pl.tolichwer.czoperkotlin.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import pl.tolichwer.czoperkotlin.ui.loginView.LoginActivityViewModel
import pl.tolichwer.czoperkotlin.ui.mapView.MapFragmentViewModel
import pl.tolichwer.czoperkotlin.ui.positionList.PositionListFragmentViewModel
import pl.tolichwer.czoperkotlin.ui.searchView.SearchFragmentViewModel

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