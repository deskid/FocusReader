package com.github.deskid.focusreader.screens.yitu

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.deskid.focusreader.R
import com.github.deskid.focusreader.api.data.ZenImage
import com.github.deskid.focusreader.widget.WebImageView

class ZenItemRecyclerViewAdapter(private val mValues: ArrayList<ZenImage>) : RecyclerView.Adapter<ZenItemRecyclerViewAdapter.ViewHolder>() {

    fun addData(data: List<ZenImage>, reverse: Boolean = false) {
        if (reverse) {
            for (image in data) {
                mValues.add(0, image)
            }
            notifyItemRangeChanged(0, data.size)
        } else {
            val index = mValues.size
            mValues.addAll(data)
            notifyItemRangeChanged(index, mValues.size)
        }

    }

    fun swipeData(data: List<ZenImage>) {

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
                .inflate(R.layout.fragment_zenimage_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.mTitleView.text = mValues[position].title
        val imgurl = mValues[position].imgurl.replace("square", "medium")
        holder.mWebImageView.setImageUrl(imgurl)
        holder.itemView.setOnClickListener {
            ZenImageDetailAct.start(holder.itemView.context, position, mValues)
        }

    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val mTitleView: TextView = mView.findViewById(R.id.title) as TextView
        val mWebImageView: WebImageView = mView.findViewById(R.id.web_imageview) as WebImageView
    }
}
