package com.github.deskid.focusreader.screens.penti.yitu

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.github.deskid.focusreader.R
import com.github.deskid.focusreader.utils.lazyFast
import com.github.deskid.focusreader.widget.image.setImageUrl
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_zenimage_detail.*

class ZenImageDetailFragment : DaggerFragment() {

    private val viewModel: ZenImageDetailViewModel by lazyFast {
        ViewModelProviders.of(this).get(ZenImageDetailViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_zenimage_detail, container, false)
    }

    override fun onViewCreated(root: View, savedInstanceState: Bundle?) {
        val pageUrl: String = arguments?.getString("URL") ?: ""
        val pageImage = arguments?.getString("IMG") ?: ""


        if (!TextUtils.isEmpty(pageUrl)) {
            viewModel.loadZenImageDetail(pageUrl)
        }

        viewModel.data.observe(this, Observer {
            description.text = it?.content
        })

        if (!TextUtils.isEmpty(pageImage)) {
            image.transitionName = pageUrl + "image"
            description.transitionName = pageUrl + "title"
            image.setImageUrl(pageImage.replace("square", "medium"), {
                val textSwatch = it?.mutedSwatch
                textSwatch?.let { swatch ->
                    if (isAdded) {
                        description.setBackgroundColor(swatch.rgb)
                        description.setTextColor(swatch.bodyTextColor)
                    }
                }
                val imageSwatch = it?.darkMutedSwatch
                imageSwatch?.let { swatch ->
                    if (isAdded) {
                        container.setBackgroundColor(swatch.rgb)
                    }
                }
            }) { bitmap ->
                activity?.startPostponedEnterTransition()
                image.setOnClickListener {
                    var dialog = ZenImagePhotoViewDlg(context, bitmap)
                    dialog.show()
                }
            }
        }
    }

    companion object {
        fun newInstance(url: String?, img: String): ZenImageDetailFragment {
            val fragment = ZenImageDetailFragment()
            val bundle = Bundle()
            bundle.putString("URL", url)
            bundle.putString("IMG", img)
            fragment.arguments = bundle
            return fragment
        }
    }
}