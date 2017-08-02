package com.github.deskid.focusreader.screens.tugua

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.deskid.focusreader.R
import com.github.deskid.focusreader.api.data.TuGua
import com.github.deskid.focusreader.utils.screenWidth
import com.github.deskid.focusreader.widget.WebImageView
import com.github.deskid.focusreader.widget.setWidth

class TuGuaItemRecyclerViewAdapter(private val mValues: MutableList<TuGua>) : RecyclerView.Adapter<TuGuaItemRecyclerViewAdapter.ViewHolder>() {

    fun addData(data: List<TuGua>) {
        val index = mValues.size
        mValues.addAll(data)
        notifyItemRangeChanged(index, mValues.size)
    }

    fun swipeData(data: List<TuGua>) {
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return mValues[oldItemPosition].description == data[newItemPosition].description
            }

            override fun getOldListSize(): Int {
                return mValues.size
            }

            override fun getNewListSize(): Int {
                return data.size
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return mValues[oldItemPosition] == data[newItemPosition]
            }

        })
        diffResult.dispatchUpdatesTo(this)
        mValues.clear()
        mValues.addAll(data)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_tuguaitem, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val width = holder.itemView.context.screenWidth

        holder.mTitleView.text = mValues[position].title.replace(Regex("【.+?】"), "")
        holder.mWebImageView.setImageUrl(mValues[position].imgurl)
        holder.mWebImageView.setWidth(width)
        holder.itemView.setOnClickListener {
            WebViewActivity.start(holder.itemView.context, mValues[position].description, mValues[position].imgurl)
        }

    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val mTitleView: TextView = mView.findViewById(R.id.title)
        val mWebImageView: WebImageView = mView.findViewById(R.id.web_imageview)
    }
}
