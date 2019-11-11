package pl.tolichwer.czoperkotlin.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object Constants {
    const val FOO = "foo"
    const val PACKAGE_NAME = "pl.tolichwer.czoperkotlin"
    const val RECEIVER = "$PACKAGE_NAME.RECEIVER"
    const val RESULT_DATA_KEY = "$PACKAGE_NAME.RESULT_DATA_KEY"
    const val LOCATION_DATA_EXTRA = "$PACKAGE_NAME.LOCATION_DATA_EXTRA"
    const val SUCCESS_RESULT = 0
    const val FAILURE_RESULT = 1
    const val START_DATE = "START_DATE"
    const val END_DATE = "END_DATE"

    val CURRENT_DAY = getCurrentDay()

    enum class PositionStatus {
        MOVE, STOP, UNKNOWN, PAUSE,
    }

    fun longToString(date: Long): String{
        val data = Date(date)
        val sdf = SimpleDateFormat("HH:mm:ss", Locale("pl"))

        return sdf.format(data)
    }

    private fun getCurrentDay(): String {
        val date = Date(System.currentTimeMillis())
        return SimpleDateFormat("dd MMM", Locale("pl")).format(date)
    }


}

