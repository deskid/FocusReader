package com.github.deskid.focusreader.screens.readhub.topic

import android.annotation.SuppressLint
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.deskid.focusreader.R
import com.github.deskid.focusreader.api.data.Topic
import com.github.deskid.focusreader.utils.fromNow
import com.github.deskid.focusreader.utils.launchUrlWithCustomTabs
import com.github.deskid.focusreader.utils.toDate
import com.github.deskid.focusreader.utils.withoutSuffix
import com.github.deskid.focusreader.widget.ReadMoreTextView

class TopicAdapter : RecyclerView.Adapter<TopicAdapter.ViewHolder> {
    private val topics: MutableList<Topic>

    constructor(topics: MutableList<Topic>) : super() {
        this.topics = topics.toMutableList()
    }

    override fun getItemCount(): Int = topics.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: TopicAdapter.ViewHolder, position: Int) {
        val index = holder.adapterPosition
        holder.titleView.text = topics[index].title
        if (topics[index].summary.isEmpty()) {
            holder.contentView.text = "暂无摘要"
        } else {
            holder.contentView.text = topics[index].summary
        }
        holder.contentView.setExpanded(topics[index].readMore)
        holder.contentView.onReadMoreChanged = {
            topics[index].readMore = it
            notifyItemChanged(position)
        }
        holder.publishDateView.text = "${topics[index].publishDate.withoutSuffix().toDate().fromNow()}前"

        holder.siteView.text = topics[index].newsArray[0].siteName
        val context = holder.itemView.context
        holder.itemView.setOnClickListener {
            context.launchUrlWithCustomTabs(topics[index].newsArray[0].mobileUrl)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopicAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_readhub_topic_item, parent, false)
        return ViewHolder(view)
    }

    fun addData(data: List<Topic>) {
        val set = LinkedHashSet<Topic>(topics)
        set.addAll(data)
        swipeData(ArrayList(set))
    }

    fun swipeData(data: List<Topic>) {
        val diffResult: DiffUtil.DiffResult = DiffUtil.calculateDiff(object : DiffUtil.Callback() {
            override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return topics[oldItemPosition].id == data[newItemPosition].id
            }

            override fun getOldListSize(): Int {
                return topics.size
            }

            override fun getNewListSize(): Int {
                return data.size
            }

            override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
                return topics[oldItemPosition] == data[newItemPosition]
            }

        })
        diffResult.dispatchUpdatesTo(this)
        topics.clear()
        topics.addAll(data)
    }

    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val titleView: TextView = mView.findViewById(R.id.title)
        val publishDateView: TextView = mView.findViewById(R.id.publish_date)
        val contentView: ReadMoreTextView = mView.findViewById(R.id.content)
        val siteView: TextView = mView.findViewById(R.id.instant_read)
    }

}