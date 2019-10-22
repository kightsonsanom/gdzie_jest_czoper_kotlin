package pl.tolichwer.czoperkotlin.ui.positionList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pl.tolichwer.czoperkotlin.db.Repository
import pl.tolichwer.czoperkotlin.model.User
import javax.inject.Inject

enum class PositionType { MOVE, STOP, UNKNOWN, PAUSE }

class PositionListFragmentViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private var _users = MutableLiveData<List<User>>()

    val users: LiveData<List<User>>
        get() = _users

    init {
        getUsersForList()
    }

    private fun getUsersForList() {
          repository.getUsersFromDB()
              .subscribe {
                  _users.postValue(it)
              }

    }
}