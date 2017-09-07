package com.github.deskid.focusreader.screens.yitu

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.github.deskid.focusreader.R
import com.github.deskid.focusreader.api.data.ZenImage
import com.github.deskid.focusreader.widget.WebImageView
import com.github.deskid.focusreader.widget.show

class ZenItemRecyclerViewAdapter(private val mValues: ArrayList<ZenImage>) : RecyclerView.Adapter<ZenItemRecyclerViewAdapter.ViewHolder>() {

    private var mListener: ((position: Int, textView: TextView, imageView: ImageView, images: ArrayList<ZenImage>) -> Unit)? = null

    fun addData(data: List<ZenImage>) {
        val set = LinkedHashSet<ZenImage>(mValues)
        set.addAll(data)
        swipeData(ArrayList<ZenImage>(set))
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

    fun setOnClickListener(block: (position: Int, textView: TextView, imageView: ImageView, images: ArrayList<ZenImage>) -> Unit) {
        mListener = block
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_zenimage_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.mTitleView.text = mValues[position].title

        holder.mTitleView.transitionName = mValues[position].url+"title"
        holder.mWebImageView.transitionName = mValues[position].url+"image"

        val imgurl = mValues[position].imgurl.replace("square", "medium")
        holder.mWebImageView.setImageUrl(imgurl, {
            val textSwatch = it?.mutedSwatch
            textSwatch?.let { swatch ->
                holder.mTitleView.setBackgroundColor(swatch.rgb)
                holder.mTitleView.setTextColor(swatch.bodyTextColor)
            }
        }, {
            holder.mTitleView.show()
        })

        holder.itemView.setOnClickListener {
            mListener?.invoke(position, holder.mTitleView, holder.mWebImageView, mValues)
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
