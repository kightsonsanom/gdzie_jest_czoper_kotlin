package pl.tolichwer.czoperkotlin.ui.positionList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.DaggerFragment
import pl.tolichwer.czoperkotlin.R
import pl.tolichwer.czoperkotlin.databinding.PositionListFragmentBinding
import pl.tolichwer.czoperkotlin.di.ViewModelFactory
import javax.inject.Inject

class PositionListFragment : DaggerFragment() {

    private lateinit var binding: PositionListFragmentBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var viewModel: PositionListFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.position_list_fragment, container, false)

        viewModel =
            activity?.run { ViewModelProvider(this, viewModelFactory).get(PositionListFragmentViewModel::class.java) }
                ?: throw Exception("Invalid Activity")

        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        return binding.root
    }
}

