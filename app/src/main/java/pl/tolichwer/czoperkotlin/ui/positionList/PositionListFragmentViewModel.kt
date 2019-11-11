package pl.tolichwer.czoperkotlin.ui.positionList

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import pl.tolichwer.czoperkotlin.db.Repository
import pl.tolichwer.czoperkotlin.model.User
import pl.tolichwer.czoperkotlin.util.Constants
import javax.inject.Inject

enum class PositionType { MOVE, STOP, UNKNOWN, PAUSE }

class PositionListFragmentViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    val currentDay = Constants.CURRENT_DAY
    private val _selectedUser = MutableLiveData<User>()

    val selectedUser: LiveData<User>
        get() = _selectedUser

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

    fun setCurrentUser(user: User) {
        _selectedUser.value = user
    }

}