package com.github.deskid.focusreader.screens.zhihudaily

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.deskid.focusreader.R
import com.github.deskid.focusreader.api.data.Story
import com.github.deskid.focusreader.widget.WebImageView

class ZhihuAdapter(private val mValues: ArrayList<Story>) : RecyclerView.Adapter<ZhihuAdapter.ViewHolder>() {
    override fun getItemCount(): Int = mValues.size

    override fun onBindViewHolder(holder: ZhihuAdapter.ViewHolder, position: Int) {
        holder.mTitleView.text = mValues[position].title
        val imgurl = mValues[position].images[0]
        holder.mWebImageView.setImageUrl(imgurl)
        holder.itemView.setOnClickListener {
            ZhihuWebViewActivity.start(holder.itemView.context, mValues[position].id.toString())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ZhihuAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_zhihu_item, parent, false)
        return ViewHolder(view)
    }

    fun swipeData(data: List<Story>) {

        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return mValues[oldItemPosition].id == data[newItemPosition].id
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

    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val mTitleView: TextView = mView.findViewById(R.id.title)
        val mWebImageView: WebImageView = mView.findViewById(R.id.web_imageview)
    }

}