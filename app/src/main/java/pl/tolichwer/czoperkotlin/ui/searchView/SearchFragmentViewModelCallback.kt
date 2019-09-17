package pl.tolichwer.czoperkotlin.ui.searchView

import java.util.Date

interface SearchFragmentViewModelCallback {
    fun setDate(dates: List<Date>)
}
