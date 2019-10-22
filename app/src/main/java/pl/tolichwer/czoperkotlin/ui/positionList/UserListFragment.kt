package pl.tolichwer.czoperkotlin.ui.positionList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import dagger.android.support.DaggerFragment
import pl.tolichwer.czoperkotlin.R
import pl.tolichwer.czoperkotlin.databinding.UserListFragmentBinding
import pl.tolichwer.czoperkotlin.di.ViewModelFactory
import javax.inject.Inject

class UserListFragment : DaggerFragment(){


    private lateinit var binding: UserListFragmentBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: PositionListFragmentViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(PositionListFragmentViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.user_list_fragment, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.userList.adapter = UserListAdapter()

        return binding.root
    }
}