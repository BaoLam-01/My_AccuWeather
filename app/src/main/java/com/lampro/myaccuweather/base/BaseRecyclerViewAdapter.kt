package com.lampro.weatherapp.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerViewAdapter<T : Any, VBD : ViewDataBinding> :
    RecyclerView.Adapter<BaseRecyclerViewAdapter.BaseViewHolder<VBD>>() {
    class BaseViewHolder<VBD : ViewDataBinding>(var binding: VBD) :
        RecyclerView.ViewHolder(binding.root)

    var mListData = mutableListOf<T>()

    lateinit var mContext: Context
    fun updateData(data: List<T>) {
        this.mListData = data as MutableList<T>
        notifyDataSetChanged()
    }

    var listener: ((view: View, item: T, position: Int) -> Unit)? = null
    abstract fun getLayout(): Int

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<VBD> {
        this.mContext = parent.context
        return BaseViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                getLayout(),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        if (mListData.isEmpty()) {
            return 0
        }
        return mListData.size
    }

    override fun onBindViewHolder(holder: BaseViewHolder<VBD>, position: Int) {
    }
}