package pl.tolichwer.czoperkotlin.ui.positionList

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import dagger.android.support.DaggerFragment
import pl.tolichwer.czoperkotlin.R
import pl.tolichwer.czoperkotlin.databinding.UserListFragmentBinding
import pl.tolichwer.czoperkotlin.di.ViewModelFactory
import javax.inject.Inject

class UserListFragment : DaggerFragment() {

    private lateinit var binding: UserListFragmentBinding

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var viewModel: PositionListFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        binding = UserListFragmentBinding.inflate(inflater)

        viewModel =
            activity?.run { ViewModelProvider(this, viewModelFactory).get(PositionListFragmentViewModel::class.java) }
                ?: throw Exception("Invalid Activity")

        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        binding.userList.adapter = UserListAdapter(UserListAdapter.OnClickListener{
            viewModel.setCurrentUser(it)
            findNavController().navigate(R.id.next_destination)
            Log.d("UserListFragment"," user = $it")

        })

        return binding.root
    }
}