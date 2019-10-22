package pl.tolichwer.czoperkotlin.ui.mapView

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import pl.tolichwer.czoperkotlin.db.Repository
import javax.inject.Inject

class MapFragmentViewModel @Inject constructor(
    private val repository: Repository,
    private val app: Application
): AndroidViewModel(app) {




    fun initGeoService() {

    }
}