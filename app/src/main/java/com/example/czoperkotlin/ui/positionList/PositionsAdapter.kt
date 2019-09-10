package com.example.czoperkotlin.ui.positionList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.czoperkotlin.R
import com.example.czoperkotlin.databinding.NieznanyItemBinding
import com.example.czoperkotlin.databinding.PostojItemBinding
import com.example.czoperkotlin.databinding.PrzerwaItemBinding
import com.example.czoperkotlin.databinding.RuchItemBinding
import com.example.czoperkotlin.model.Position

class PositionsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            0 -> {
                val ruchViewHolder = holder as RuchViewHolder
                ruchViewHolder.binding.setPosition(positionList!![position])
                ruchViewHolder.binding.executePendingBindings()
            }
            1 -> {
                val postojViewHolder = holder as PostojViewHolder
                postojViewHolder.binding.setPosition(positionList!![position])
                postojViewHolder.binding.executePendingBindings()
            }
            2 -> {
                val nieznanyVieHolder = holder as NieznanyVieHolder
                nieznanyVieHolder.binding.setPosition(positionList!![position])
                nieznanyVieHolder.binding.executePendingBindings()
            }
            3 -> {
                val przerwaViewHolder = holder as PrzerwaViewHolder
                przerwaViewHolder.binding.setPosition(positionList!![position])
                przerwaViewHolder.binding.executePendingBindings()
            }
        }
    }

    override fun getItemCount(): Int {
        return if (positionList == null) 0 else positionList!!.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        when (viewType) {
            0 -> {
                val ruchItemBinding: RuchItemBinding =
                    DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.ruch_item, parent, false)
                return RuchViewHolder(ruchItemBinding)
            }
            1 -> {
                val postojItemBinding: PostojItemBinding =
                    DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.postoj_item, parent, false)
                return PostojViewHolder(postojItemBinding)
            }
            2 -> {
                val nieznanyItemBinding: NieznanyItemBinding =
                    DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.nieznany_item, parent, false)
                return NieznanyVieHolder(nieznanyItemBinding)
            }
            3 -> {
                val przerwaItemBinding: PrzerwaItemBinding =
                    DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.przerwa_item, parent, false)
                return PrzerwaViewHolder(przerwaItemBinding)
            }
            else -> {
                val nieznanyItemBinding: NieznanyItemBinding =
                    DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.nieznany_item, parent, false)
                return NieznanyVieHolder(nieznanyItemBinding)
            }
        }
    }

    private var positionList: List<Position>? = null

    fun setPositionsList(positionList: List<Position>) {
        if (this.positionList == null) {
            this.positionList = positionList
            notifyItemRangeInserted(0, positionList.size)
        } else {
            val result = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
                override fun getOldListSize(): Int {
                    return this@PositionsAdapter.positionList!!.size
                }

                override fun getNewListSize(): Int {
                    return positionList.size
                }

                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    return this@PositionsAdapter.positionList!![oldItemPosition].id == positionList[newItemPosition].id
                }

                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                    val newAction = positionList[newItemPosition]
                    val oldAction = this@PositionsAdapter.positionList!![oldItemPosition]
                    return newAction.id == oldAction.id
                }
            })
            this.positionList = positionList
            result.dispatchUpdatesTo(this)
        }
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return positionList!![position].status
    }

    inner class RuchViewHolder(internal val binding: RuchItemBinding) : RecyclerView.ViewHolder(binding.root)

    inner class PostojViewHolder(internal val binding: PostojItemBinding) : RecyclerView.ViewHolder(binding.root)

    inner class PrzerwaViewHolder(internal val binding: PrzerwaItemBinding) : RecyclerView.ViewHolder(binding.root)

    inner class NieznanyVieHolder(internal val binding: NieznanyItemBinding) : RecyclerView.ViewHolder(binding.root)
}