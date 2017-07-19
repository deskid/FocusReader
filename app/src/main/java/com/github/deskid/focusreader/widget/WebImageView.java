package com.github.deskid.focusreader.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.squareup.picasso.Picasso;

public class WebImageView extends AppCompatImageView {

    private String mImageUrl;
    private int mResource;


    public WebImageView(Context context) {
        this(context, null);
    }

    public WebImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setImageResource(int imageResource) {
        super.setImageResource(imageResource);
        mResource = imageResource;
        mImageUrl = null;
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
    }

    public void setImageUrl(String url) {
        if (TextUtils.isEmpty(url)) {
            mImageUrl = null;
            Picasso.with(getContext()).cancelRequest(this);
            setImageBitmap(null);
        } else {
            mImageUrl = url;
            if (isAttachedToWindow()) {
                Picasso.with(getContext()).load(Uri.parse(url)).into(this);
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!TextUtils.isEmpty(mImageUrl)) {
            setImageUrl(mImageUrl);
        } else if (mResource != -1) {
            setImageResource(mResource);
        }

    }

    @Override
    protected void onDetachedFromWindow() {
        Picasso.with(getContext()).cancelRequest(this);
        super.onDetachedFromWindow();
    }
}
