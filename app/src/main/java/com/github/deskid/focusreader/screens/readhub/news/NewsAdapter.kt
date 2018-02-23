package com.github.deskid.focusreader.screens.readhub.news

import android.annotation.SuppressLint
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.deskid.focusreader.R
import com.github.deskid.focusreader.api.data.SimpleTopic
import com.github.deskid.focusreader.utils.fromNow
import com.github.deskid.focusreader.utils.launchUrlWithCustomTabs
import com.github.deskid.focusreader.utils.sub
import com.github.deskid.focusreader.utils.toDate
import com.github.deskid.focusreader.widget.ReadMoreTextView

class NewsAdapter(private val topics: MutableList<SimpleTopic>) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {
    override fun getItemCount(): Int = topics.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val index = holder.adapterPosition
        holder.bindData(topics[index])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_readhub_topic_item, parent, false)
        return ViewHolder(view)
    }

    fun addData(data: List<SimpleTopic>) {
        val set = LinkedHashSet<SimpleTopic>(topics)
        set.addAll(data)
        swipeData(ArrayList(set))
    }

    fun swipeData(data: List<SimpleTopic>) {
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

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val titleView: TextView = view.findViewById(R.id.title)
        private val publishDateView: TextView = view.findViewById(R.id.publish_date)
        private val contentView: ReadMoreTextView = view.findViewById(R.id.content)
        private val siteView: TextView = view.findViewById(R.id.instant_read)


        @SuppressLint("SetTextI18n")
        fun bindData(topic: SimpleTopic) {
            val context = itemView.context

            titleView.text = topic.title
            if (topic.summary.isEmpty()) {
                contentView.text = "暂无摘要"
            } else {
                contentView.text = topic.summary
            }
            contentView.setExpanded((contentView.tag as Boolean?) ?: true)
            publishDateView.text = "${topic.publishDate.sub().toDate().fromNow()}前"

            siteView.text = topic.siteName

            itemView.setOnClickListener {
                context.launchUrlWithCustomTabs(topic.url)
            }


        }
    }

}