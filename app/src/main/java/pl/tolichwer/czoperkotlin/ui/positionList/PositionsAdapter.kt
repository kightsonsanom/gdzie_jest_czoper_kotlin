package pl.tolichwer.czoperkotlin.ui.positionList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import pl.tolichwer.czoperkotlin.R
import pl.tolichwer.czoperkotlin.databinding.MoveItemBinding
import pl.tolichwer.czoperkotlin.databinding.PauseItemBinding
import pl.tolichwer.czoperkotlin.databinding.StopItemBinding
import pl.tolichwer.czoperkotlin.databinding.UnknownItemBinding
import pl.tolichwer.czoperkotlin.model.Position

class PositionsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private lateinit var positionList: List<Position>

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder.itemViewType) {
            0 -> {
                val moveViewHolder = holder as MoveItemViewHolder
                moveViewHolder.binding.setPosition(positionList[position])
                moveViewHolder.binding.executePendingBindings()
            }
            1 -> {
                val postojViewHolder = holder as StopItemViewHolder
                postojViewHolder.binding.setPosition(positionList[position])
                postojViewHolder.binding.executePendingBindings()
            }
            2 -> {
                val nieznanyVieHolder = holder as UnknownItemVieHolder
                nieznanyVieHolder.binding.setPosition(positionList[position])
                nieznanyVieHolder.binding.executePendingBindings()
            }
            3 -> {
                val przerwaViewHolder = holder as PauseItemViewHolder
                przerwaViewHolder.binding.setPosition(positionList[position])
                przerwaViewHolder.binding.executePendingBindings()
            }
        }
    }

    override fun getItemCount(): Int {
        return if (positionList == null) 0 else positionList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        when (viewType) {
            0 -> {
                val moveItemBinding: MoveItemBinding =
                    DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.move_item, parent, false)
                return MoveItemViewHolder(moveItemBinding)
            }
            1 -> {
                val stopItemBinding: StopItemBinding =
                    DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.stop_item, parent, false)
                return StopItemViewHolder(stopItemBinding)
            }
            2 -> {
                val unknownItemBinding: UnknownItemBinding =
                    DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.unknown_item, parent, false)
                return UnknownItemVieHolder(unknownItemBinding)
            }
            3 -> {
                val pauseItemBinding: PauseItemBinding =
                    DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.pause_item, parent, false)
                return PauseItemViewHolder(pauseItemBinding)
            }
            else -> {
                val unknownItemBinding: UnknownItemBinding =
                    DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.unknown_item, parent, false)
                return UnknownItemVieHolder(unknownItemBinding)
            }
        }
    }


    fun setPositionsList(positionList: List<Position>) {
        if (this.positionList.isEmpty()) {
            this.positionList = positionList
            notifyItemRangeInserted(0, positionList.size)

        } else {
            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int {
                    return this@PositionsAdapter.positionList.size
                }

                override fun getNewListSize(): Int {
                    return positionList.size
                }

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return this@PositionsAdapter.positionList[oldItemPosition].id == positionList[newItemPosition].id
                }

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val newAction = positionList[newItemPosition]
                    val oldAction = this@PositionsAdapter.positionList[oldItemPosition]
                    return newAction.id == oldAction.id
                }
            })
            this.positionList = positionList
            result.dispatchUpdatesTo(this)
        }
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return positionList[position].status
    }

    inner class MoveItemViewHolder(val binding: MoveItemBinding) : RecyclerView.ViewHolder(binding.root)

    inner class StopItemViewHolder(val binding: StopItemBinding) : RecyclerView.ViewHolder(binding.root)

    inner class PauseItemViewHolder(val binding: PauseItemBinding) : RecyclerView.ViewHolder(binding.root)

    inner class UnknownItemVieHolder(val binding: UnknownItemBinding) : RecyclerView.ViewHolder(binding.root)
}