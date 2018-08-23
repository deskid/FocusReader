package com.github.deskid.focusreader.screens.penti.duanzi

import android.os.Build
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.github.deskid.focusreader.R
import com.github.deskid.focusreader.api.data.Duanzi

class DuanziItemRecyclerViewAdapter(private var mValues: MutableList<Duanzi>) : RecyclerView.Adapter<DuanziItemRecyclerViewAdapter.ViewHolder>() {

    fun addData(data: List<Duanzi>) {
        val set = LinkedHashSet<Duanzi>(mValues)
        set.addAll(data)
        swipeData(ArrayList<Duanzi>(set))
    }

    fun swipeData(data: List<Duanzi>) {

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DuanziItemRecyclerViewAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_jokeitem, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: DuanziItemRecyclerViewAdapter.ViewHolder, position: Int) {
        holder.mTitleView.text = mValues[position].title

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.mContentView.text = Html.fromHtml(mValues[position].description, 0)
        } else {
            holder.mContentView.text = Html.fromHtml(mValues[position].description)
        }

    }

    override fun getItemCount(): Int {
        return mValues.size
    }

    inner class ViewHolder(mView: View) : RecyclerView.ViewHolder(mView) {
        val mTitleView: TextView = mView.findViewById(R.id.title)
        val mContentView: TextView = mView.findViewById(R.id.content)

    }
}
