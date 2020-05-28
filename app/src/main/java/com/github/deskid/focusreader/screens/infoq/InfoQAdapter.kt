package com.github.deskid.focusreader.screens.infoq

import android.app.Activity
import android.arch.paging.PagedListAdapter
import android.support.v7.util.DiffUtil.ItemCallback
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.deskid.focusreader.R
import com.github.deskid.focusreader.db.entity.InfoqArticleEntity
import com.github.deskid.focusreader.utils.launchUrlWithCustomTabs

class InfoQAdapter(private val activity: Activity) :
        PagedListAdapter<InfoqArticleEntity, InfoQAdapter.ViewHolder>(InfoqDiffUtilCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { article ->
            holder.mTitleView.text = article.title
            holder.mSummary.text = article.summary

            holder.itemView.setOnClickListener {
                activity.launchUrlWithCustomTabs(article.link)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_infoq_item, parent, false)
        return ViewHolder(view)
    }

    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val mTitleView: TextView = mView.findViewById(R.id.title)
        val mSummary: TextView = mView.findViewById(R.id.summary)
    }

    class InfoqDiffUtilCallback : ItemCallback<InfoqArticleEntity>() {
        override fun areItemsTheSame(oldItem: InfoqArticleEntity, newItem: InfoqArticleEntity): Boolean {
            return oldItem.aid == newItem.aid
        }

        override fun areContentsTheSame(oldItem: InfoqArticleEntity, newItem: InfoqArticleEntity): Boolean {
            return oldItem.summary == newItem.summary
                    && oldItem.title == newItem.title
        }
    }
}
