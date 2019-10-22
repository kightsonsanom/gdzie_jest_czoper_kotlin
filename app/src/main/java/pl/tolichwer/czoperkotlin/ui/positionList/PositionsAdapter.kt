package pl.tolichwer.czoperkotlin.ui.positionList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import pl.tolichwer.czoperkotlin.databinding.MoveItemBinding
import pl.tolichwer.czoperkotlin.databinding.PauseItemBinding
import pl.tolichwer.czoperkotlin.databinding.StopItemBinding
import pl.tolichwer.czoperkotlin.databinding.UnknownItemBinding
import pl.tolichwer.czoperkotlin.model.Position

class PositionsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Position>() {

        override fun areItemsTheSame(oldItem: Position, newItem: Position): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Position, newItem: Position): Boolean {
            return oldItem.id == newItem.id
        }
    }

    private val differ = AsyncListDiffer(this, DIFF_CALLBACK)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viewHolder: RecyclerView.ViewHolder =
            UnknownItemVieHolder(UnknownItemBinding.inflate(LayoutInflater.from(parent.context)))

        when (viewType) {
            PositionType.MOVE.ordinal ->
                viewHolder = MoveItemViewHolder(MoveItemBinding.inflate(LayoutInflater.from(parent.context)))

            PositionType.STOP.ordinal ->
                viewHolder = StopItemViewHolder(StopItemBinding.inflate(LayoutInflater.from(parent.context)))

            PositionType.UNKNOWN.ordinal ->
                viewHolder = UnknownItemVieHolder(UnknownItemBinding.inflate(LayoutInflater.from(parent.context)))

            PositionType.PAUSE.ordinal ->
                viewHolder = PauseItemViewHolder(PauseItemBinding.inflate(LayoutInflater.from(parent.context)))

        }

        return viewHolder
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when(holder.itemViewType){
            PositionType.MOVE.ordinal -> (holder as MoveItemViewHolder).bind(differ.currentList[position])
            PositionType.STOP.ordinal -> (holder as StopItemViewHolder).bind(differ.currentList[position])
            PositionType.UNKNOWN.ordinal -> (holder as UnknownItemVieHolder).bind(differ.currentList[position])
            PositionType.PAUSE.ordinal -> (holder as PauseItemViewHolder).bind(differ.currentList[position])
        }

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun getItemViewType(position: Int): Int {
        return differ.currentList[position].status
    }

    fun submitList(list: List<Position>) {
        differ.submitList(list)
    }

    class MoveItemViewHolder(val binding: MoveItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(position: Position){
            binding.position = position
            binding.executePendingBindings()
        }
    }

    class StopItemViewHolder(val binding: StopItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(position: Position){
            binding.position = position
            binding.executePendingBindings()
        }
    }

    class PauseItemViewHolder(val binding: PauseItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(position: Position){
            binding.position = position
            binding.executePendingBindings()
        }
    }

    class UnknownItemVieHolder(val binding: UnknownItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(position: Position){
            binding.position = position
            binding.executePendingBindings()
        }
    }

}