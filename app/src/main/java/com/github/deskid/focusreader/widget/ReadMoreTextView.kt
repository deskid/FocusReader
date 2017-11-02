package com.github.deskid.focusreader.widget

import android.content.Context
import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.TextPaint
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.view.View
import android.view.ViewTreeObserver
import android.widget.TextView
import com.github.deskid.focusreader.R
import com.github.deskid.focusreader.utils.getColorCompat
import com.github.logutils.LogUtils

class ReadMoreTextView : TextView {

    @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {
        trimLines = DEFAULT_TRIM_LINES
        val a = context.obtainStyledAttributes(attrs, R.styleable.ReadMoreTextView)
        trimLines = a.getInt(R.styleable.ReadMoreTextView_trimLines, DEFAULT_TRIM_LINES)
        collapsedText = context.getString(a.getResourceId(R.styleable.ReadMoreTextView_trimCollapsedText, R.string.show_all_content))
        expandedText = context.getString(a.getResourceId(R.styleable.ReadMoreTextView_trimExpandedText, R.string.hide_content))
        trimTextColor = a.getColor(R.styleable.ReadMoreTextView_trimTextColor, context.getColorCompat(R.color.colorPrimaryLight))
        enableCollapsedText = a.getBoolean(R.styleable.ReadMoreTextView_enableCollapsedText, DEFAULT_ENABLE_COLLAPSED_TEXT)
        a.recycle()

        ellipsize = TextUtils.TruncateAt.END
        viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                viewTreeObserver.removeOnPreDrawListener(this)

                lineEndIndex = when (trimLines) {
                    0 -> layout?.getLineEnd(0) ?: 0
                    in 1..lineCount -> layout?.getLineEnd(trimLines - 1) ?: 0
                    else -> layout?.getLineEnd(lineCount - 1) ?: 0
                }

                setTrimedText()
                return true
            }
        })

    }

    private val DEFAULT_TRIM_LINES = 6
    private val DEFAULT_ENABLE_COLLAPSED_TEXT = true
    private val ELLIPSIZE = "..."

    private val LINE_END = System.lineSeparator()

    private var readMore = true

    private var trimLines: Int
    private var collapsedText = ""
    private var expandedText = ""
    private var trimTextColor = 0
    private var enableCollapsedText = true

    private var lineEndIndex = 0
    private var bufferType: BufferType? = BufferType.NORMAL
    private var originalText: CharSequence? = ""

    override fun setText(text: CharSequence?, type: BufferType?) {
        originalText = text
        bufferType = type
        setTrimedText()
        movementMethod = LinkMovementMethod.getInstance()
        highlightColor = Color.TRANSPARENT
    }

    private fun setTrimedText() {
        if (lineEndIndex > 0) {
            val s: SpannableStringBuilder
            if (readMore) {

                var trimEndIndex = lineEndIndex - (ELLIPSIZE.length)
                if (originalText?.length ?: 0 < trimEndIndex) {
                    return
                }
                s = SpannableStringBuilder(originalText, 0, trimEndIndex)
                        .append(ELLIPSIZE)
                        .append(LINE_END)
                        .append(collapsedText)

                s.setSpan(ToggleSpan(), s.length - collapsedText.length, s.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                super.setText(s, bufferType)
            } else {
                if (enableCollapsedText) {
                    s = SpannableStringBuilder(originalText, 0, originalText?.length ?: 0)
                            .append(LINE_END)
                            .append(expandedText)

                    s.setSpan(ToggleSpan(), s.length - expandedText.length, s.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    super.setText(s, bufferType)
                } else {
                    super.setText(originalText, bufferType)
                }
            }
        } else {
            super.setText(originalText, bufferType)
        }
    }

    inner class ToggleSpan : ClickableSpan() {
        override fun onClick(widget: View?) {
            setExpanded(!readMore)
            requestFocusFromTouch()
        }

        override fun updateDrawState(ds: TextPaint) {
            ds.color = trimTextColor
            ds.isUnderlineText = false
        }
    }

    fun setExpanded(readMore: Boolean) {
        this.readMore = readMore
        LogUtils.d("setExpanded : " + readMore)
        tag = readMore
        setTrimedText()
    }
}