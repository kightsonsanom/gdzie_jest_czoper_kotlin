package pl.tolichwer.czoperkotlin.ui.positionList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dagger.android.support.DaggerFragment
import pl.tolichwer.czoperkotlin.databinding.UserListFragmentBinding
import pl.tolichwer.czoperkotlin.di.ViewModelFactory
import javax.inject.Inject

class UserListFragment : DaggerFragment() {

    private lateinit var binding: UserListFragmentBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: PositionListFragmentViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(PositionListFragmentViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = UserListFragmentBinding.inflate(inflater)

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        binding.userList.adapter = UserListAdapter(UserListAdapter.OnClickListener{
            findNavController().navigate(UserListFragmentDirections.nextDestination(it))
        })

        return binding.root
    }
}