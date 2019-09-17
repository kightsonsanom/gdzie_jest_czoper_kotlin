package pl.tolichwer.czoperkotlin.ui.searchView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.databinding.DataBindingUtil
import dagger.android.support.DaggerFragment
import pl.tolichwer.czoperkotlin.R
import pl.tolichwer.czoperkotlin.databinding.SearchFragmentBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SearchFragment : DaggerFragment(), SearchFragmentViewModelCallback {

    private lateinit var binding: SearchFragmentBinding
    private lateinit var userSpinner: Spinner

    val mockUserList = mutableListOf("Tomek", "Marek", "Pawel")

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.search_fragment, container, false)


        setOnClickListeners()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userSpinner = view.findViewById(R.id.user_spinner)

        initUserSpinner()
    }

    private fun initUserSpinner() {
        val userNamesAdapter = ArrayAdapter<String>(activity, android.R.layout.simple_list_item_1)
        userNamesAdapter.addAll(mockUserList)

        userSpinner.adapter = userNamesAdapter
    }

    private fun setOnClickListeners() {
        binding.btnChooseDate.setOnClickListener {
            val datePickerFragment = DatePickerFragment.newInstance("Tytul")
            datePickerFragment.setDatePickerCallback(this)
            datePickerFragment.show(activity!!.supportFragmentManager, "dialogFragment")

        }
    }

    override fun setDate(dates: List<Date>) {

        val dateText = if (dates.size ==1) {
            parseDate(dates[0])
        } else {
            "${parseDate(dates[0])} - ${parseDate(dates[dates.size-1])}"
        }

        binding.btnChooseDate.text = dateText

    }

    private fun parseDate(date: Date): String{
        val oldDateFormat = SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH)
        val newDateFormat = SimpleDateFormat("d MMM", Locale.getDefault())

        val oldDateParse = oldDateFormat.parse(date.toString())

        return newDateFormat.format(oldDateParse)
    }

}