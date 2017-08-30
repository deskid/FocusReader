package com.github.deskid.focusreader.screens.yitu

import android.arch.lifecycle.LifecycleFragment
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import com.github.deskid.focusreader.R
import com.github.deskid.focusreader.app.App
import kotlinx.android.synthetic.main.fragment_zenimage_detail.*
import javax.inject.Inject

class ZenImageDetailFragment : LifecycleFragment() {
    @Inject
    lateinit var factory: ZenImageViewModel.ZenImageFactory

    val viewModel: ZenImageViewModel by lazy {
        ViewModelProviders.of(this, factory).get(ZenImageViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (context.applicationContext as App).appComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_zenimage_detail, container, false)
    }

    override fun onViewCreated(root: View?, savedInstanceState: Bundle?) {
        val pageUrl: String = arguments.getString("URL")
        val pageImage = arguments.getString("IMG")

        if (!TextUtils.isEmpty(pageUrl)) {
            viewModel.loadYituDetail(pageUrl).observe(this, Observer {
                description.text = it?.content
            })
        }

        if (!TextUtils.isEmpty(pageImage)) {
            image.transitionName = pageUrl
            description.transitionName = pageUrl
            image.setImageUrl(pageImage.replace("square", "medium"), {
                val textSwatch = it?.darkMutedSwatch
                textSwatch?.let { swatch ->
                    if (isAdded) {
                        description.setBackgroundColor(swatch.rgb)
                        description.setTextColor(swatch.bodyTextColor)
                    }
                }
            }) {
                image.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                    override fun onPreDraw(): Boolean {
                        image.viewTreeObserver.removeOnPreDrawListener(this)
                        activity.startPostponedEnterTransition()
                        return true
                    }
                })
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