package pl.tolichwer.czoperkotlin.ui.positionList

import androidx.lifecycle.ViewModel
import pl.tolichwer.czoperkotlin.db.Repository
import io.reactivex.Flowable
import javax.inject.Inject

class PositionListFragmentViewModel @Inject constructor(
    private val repository: Repository
): ViewModel() {

    fun getFlowableUserNames(): Flowable<List<String>> {
        return repository.getUserNamesFromDB()
    }
}