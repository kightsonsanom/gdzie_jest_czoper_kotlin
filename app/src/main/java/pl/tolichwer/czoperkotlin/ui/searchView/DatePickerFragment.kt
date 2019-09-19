package pl.tolichwer.czoperkotlin.ui.searchView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.DialogFragment
import com.savvi.rangedatepicker.CalendarPickerView
import pl.tolichwer.czoperkotlin.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class DatePickerFragment : DialogFragment() {

    private lateinit var searchFragmentViewModelCallback: SearchFragmentViewModelCallback

    fun setDatePickerCallback(searchFragmentViewModelCallback: SearchFragmentViewModelCallback) {
        this.searchFragmentViewModelCallback = searchFragmentViewModelCallback
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.date_picker_fragment, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val calendar = view.findViewById<CalendarPickerView>(R.id.calendar_view)
        val button = view.findViewById<Button>(R.id.accept_dates)
        val nextYear = Calendar.getInstance()
        val lastYear = Calendar.getInstance()


        nextYear.add(Calendar.YEAR, 10)
        lastYear.add(Calendar.YEAR, -10)
        calendar.init(lastYear.time, nextYear.time, SimpleDateFormat("MMMM, YYYY", Locale.getDefault())) //
            .inMode(CalendarPickerView.SelectionMode.RANGE) //

        calendar.scrollToDate(Date())


        calendar.setOnDateSelectedListener(object : CalendarPickerView.OnDateSelectedListener {
            override fun onDateSelected(date: Date?) {
                button.isEnabled = true
            }

            override fun onDateUnselected(date: Date?) {
            }
        })


        button.setOnClickListener {
            searchFragmentViewModelCallback.setDate(calendar.selectedDates)
            this.dismiss()
        }
    }

    override fun onResume() {
        super.onResume()
        val params = dialog?.window?.attributes
        params?.width = ViewGroup.LayoutParams.MATCH_PARENT
        params?.height = ViewGroup.LayoutParams.MATCH_PARENT
        dialog?.window?.attributes = params as android.view.WindowManager.LayoutParams
    }

    companion object {

        fun newInstance(title: String): DatePickerFragment {
            val args = Bundle()
            args.putSerializable("title", title)
            val instance = DatePickerFragment()
            instance.arguments = args
            return instance
        }
    }
}