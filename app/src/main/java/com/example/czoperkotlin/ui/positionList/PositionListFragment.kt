package com.example.czoperkotlin.ui.positionList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.example.czoperkotlin.R
import com.example.czoperkotlin.databinding.PositionListFragmentBinding
import com.example.czoperkotlin.di.ViewModelFactory
import dagger.android.support.DaggerFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

class PositionListFragment : DaggerFragment() {

    private lateinit var binding: PositionListFragmentBinding
    private lateinit var viewModel: PositionListFragmentViewModel
    private val mDisposable = CompositeDisposable()

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.position_list_fragment, container, false)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PositionListFragmentViewModel::class.java)


        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initUserList()
    }

    private fun initUserList() {
        val itemsAdapter: ArrayAdapter<String> = ArrayAdapter(activity!!, android.R.layout.simple_list_item_1)

        binding.userList.adapter = itemsAdapter

        mDisposable.add(
            viewModel.getFlowableUserNames()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    if (it.isNotEmpty()) {
                        itemsAdapter.addAll(it)
                    }
                }
        )

        binding.userList.setOnItemClickListener { parent, view, position, id ->

            Toast.makeText(activity, "Kliknieto na usera nr $position", Toast.LENGTH_LONG).show()






        }
    }
}

