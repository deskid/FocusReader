package com.github.deskid.focusreader.screens.zhihudaily

import android.app.Activity
import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil.ItemCallback
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.deskid.focusreader.R
import com.github.deskid.focusreader.db.entity.ZhihuDailyPostEntity
import com.github.deskid.focusreader.widget.image.WebImageView
import com.github.deskid.focusreader.widget.image.setImageUrl

class ZhihuAdapter(private val activity: Activity) :
        PagedListAdapter<ZhihuDailyPostEntity, ZhihuAdapter.ViewHolder>(ZhihuPostDiffUtilCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.mTitleView.text = item?.title
        val imgurl = item?.images?.get(0)

        holder.mWebImageView.setImageUrl(imgurl)
        holder.itemView.setOnClickListener {
            ZhihuWebViewActivity.start(activity, item?.id.toString())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_zhihu_item, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val mTitleView: TextView = mView.findViewById(R.id.title)
        val mWebImageView: WebImageView = mView.findViewById(R.id.web_imageview)
    }

    class ZhihuPostDiffUtilCallback : ItemCallback<ZhihuDailyPostEntity>() {
        override fun areItemsTheSame(oldItem: ZhihuDailyPostEntity, newItem: ZhihuDailyPostEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ZhihuDailyPostEntity, newItem: ZhihuDailyPostEntity): Boolean {
            return oldItem.date == newItem.date
                    && oldItem.title == newItem.title
        }
    }
}
