package com.github.deskid.focusreader.screens.readhub.topic

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.deskid.focusreader.R
import com.github.deskid.focusreader.api.data.Topic
import com.github.deskid.focusreader.favorite.xiaomi.*
import com.github.deskid.focusreader.screens.readhub.InstantViewActivity
import com.github.deskid.focusreader.utils.fromNow
import com.github.deskid.focusreader.utils.launchUrlWithCustomTabs
import com.github.deskid.focusreader.utils.sub
import com.github.deskid.focusreader.utils.toDate
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
        holder.bindData(topics[index])
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

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val titleView: TextView = view.findViewById(R.id.title)
        private val publishDateView: TextView = view.findViewById(R.id.publish_date)
        private val contentView: ReadMoreTextView = view.findViewById(R.id.content)
        private val siteView: TextView = view.findViewById(R.id.instant_read)
        private val favoriteTv: TextView = view.findViewById(R.id.add_favorite)
        private val shareTv: TextView = view.findViewById(R.id.share)

        @SuppressLint("SetTextI18n")
        fun bindData(topic: Topic) {
            val context = itemView.context
            titleView.text = topic.title
            if (topic.summary.isEmpty()) {
                contentView.text = "暂无摘要"
            } else {
                contentView.text = topic.summary
            }
            contentView.setExpanded(topic.readMore)
            contentView.onReadMoreChanged = {
                topic.readMore = it
                notifyItemChanged(adapterPosition)
            }
            publishDateView.text = "${topic.publishDate.sub().toDate().fromNow()}前"

            siteView.text = topic.newsArray[0].siteName

            itemView.setOnClickListener {
                if (topic.extra.instantView) {
                    InstantViewActivity.start(context, topic.id)
                } else {
                    context.launchUrlWithCustomTabs(topic.newsArray[0].mobileUrl)
                }
            }

            favoriteTv.setOnClickListener {
                //todo 增加设置项 和版本判断
                if (android.os.Build.MANUFACTURER == "Xiaomi") {
                    val bundle = Bundle()
                    val componentName = "com.github.deskid.focusreader/com.github.deskid.focusreader.screens.readhub.InstantViewActivity"
                    val uri = Uri.Builder()
                            .appendQueryParameter("topicId", topic.id)
                            .appendQueryParameter("instantView", topic.extra.instantView.toString())
                            .appendQueryParameter("mobileUrl", topic.newsArray[0].mobileUrl)
                            .build()
                    val topicBundle = Bundle()
                    topicBundle.putString("topicId", topic.id)
                    bundle.putString(MATCH_COMPONENT, componentName)
                    bundle.putString(TARGET_URL, topic.newsArray[0].mobileUrl)  // 参数类型http/https，指详情页的具体链接地址，用于对应应用卸载后，网页显示详情，webview.loadUrl(targetUrl)
                    bundle.putString(TARGET_DATA, uri.toString())  //对应详情页面的intent data,从收藏跳转到对应APP的详情页使用：Intent.setData(Uri targetData)
                    bundle.putString(TARGET_TITLE, topic.title) // 必选，收藏条目显示标题
//                bundle.putString(TARGET_IMAGE, targetImage);   //可选，收藏图片url，推荐大小180x180px(宽高比为1：1)
//                bundle.putString(TARGET_EXTRA, topic.id)  // 可选，用于有额外需求的参数传递

                    // 发送收藏广播
                    val intent = Intent(ACTION) // 指定广播Action
                    intent.putExtras(bundle)
                    intent.setPackage(PACKAGE) // 限定当前收藏广播接收者的包名和权限
                    context.sendBroadcast(intent, PERMISSION)
                } else {

                }
                //todo 添加到数据库

            }

            shareTv.setOnClickListener {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                intent.putExtra(Intent.EXTRA_TEXT, topic.title + " " + topic.newsArray[0].mobileUrl)
                context.startActivity(Intent.createChooser(intent, "分享..."))
            }

        }

    }

}