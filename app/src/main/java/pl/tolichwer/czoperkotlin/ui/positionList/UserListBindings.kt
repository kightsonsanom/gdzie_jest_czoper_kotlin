package pl.tolichwer.czoperkotlin.ui.positionList

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import pl.tolichwer.czoperkotlin.model.User


@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<User>?){
    val adapter = recyclerView.adapter as UserListAdapter
    adapter.submitList(data)
}


