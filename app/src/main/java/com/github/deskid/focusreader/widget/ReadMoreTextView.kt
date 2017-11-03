package com.github.deskid.focusreader.widget

import android.content.Context
import android.graphics.Color
import android.text.*
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.widget.TextView
import com.github.deskid.focusreader.R
import com.github.deskid.focusreader.utils.getColorCompat
import com.github.logutils.LogUtils

class ReadMoreTextView : TextView {

    companion object {
        private val DEFAULT_TRIM_LINES = 2
        private val ELLIPSIZE = "..."
        private val DEFAULT_ENABLE_COLLAPSED_TEXT = true
        private val LINE_END = System.lineSeparator()
    }

    private var trimLines: Int

    private var collapsedText: String
    private var expandedText: String
    private var trimTextColor: Int
    private var enableCollapsedText = true

    private var readMore: Boolean
    private var lineEndIndex = 0
    private lateinit var bufferType: BufferType
    private lateinit var originalText: CharSequence

    @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {

        val a = context.obtainStyledAttributes(attrs, R.styleable.ReadMoreTextView)
        trimLines = a.getInt(R.styleable.ReadMoreTextView_trimLines, DEFAULT_TRIM_LINES)
        collapsedText = context.getString(a.getResourceId(R.styleable.ReadMoreTextView_trimCollapsedText, R.string.show_all_content))
        expandedText = context.getString(a.getResourceId(R.styleable.ReadMoreTextView_trimExpandedText, R.string.hide_content))
        trimTextColor = a.getColor(R.styleable.ReadMoreTextView_trimTextColor, context.getColorCompat(R.color.colorPrimaryLight))
        enableCollapsedText = a.getBoolean(R.styleable.ReadMoreTextView_enableCollapsedText, DEFAULT_ENABLE_COLLAPSED_TEXT)
        a.recycle()

        readMore = true

        viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
            override fun onPreDraw(): Boolean {
                viewTreeObserver.removeOnPreDrawListener(this)
                getLineEndIndex()
                setTextInternal()
                return true
            }
        })

        setOnTouchListener { v, event ->
            val buffer = Spannable.Factory.getInstance().newSpannable((v as TextView).text)
            val action = event.action

            if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
                var x = event.x.toInt()
                var y = event.y.toInt()

                x -= v.totalPaddingLeft
                y -= v.totalPaddingTop

                x += v.scrollX
                y += v.scrollY

                val layout = v.layout
                val line = layout.getLineForVertical(y)
                val off = layout.getOffsetForHorizontal(line, x.toFloat())

                val links = buffer.getSpans(off, off, ClickableSpan::class.java)

                if (links.isNotEmpty()) {
                    if (action == MotionEvent.ACTION_UP) {
                        links[0].onClick(v)
                    } else if (action == MotionEvent.ACTION_DOWN) {
                        Selection.setSelection(buffer,
                                buffer.getSpanStart(links[0]),
                                buffer.getSpanEnd(links[0]))
                    }
                    return@setOnTouchListener true
                } else run { Selection.removeSelection(buffer) }
            }
            return@setOnTouchListener false
        }

        highlightColor = Color.TRANSPARENT
    }

    private fun getLineEndIndex() {
        layout?.let {
            if (lineCount < trimLines) {
                //no need to collapsed, show text as normal
                lineEndIndex = 0
                readMore = false
                return
            }

            val lastLineIndex = maxOf(minOf(trimLines, lineCount) - 1, 0)
            lineEndIndex = it.getLineVisibleEnd(lastLineIndex) - 1
            LogUtils.d("lineEndIndex : $lineEndIndex, lastLineIndex : $lastLineIndex; lineCount : $lineCount")
        }
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        originalText = text ?: ""
        bufferType = type ?: BufferType.NORMAL
        setTextInternal()

    }

    private fun setTextInternal() {
        LogUtils.d("lineEndIndex : $lineEndIndex, readMore : $readMore, originalText : $originalText")
        if (lineEndIndex > 0 && originalText.isNotEmpty()) {
            if (readMore) {
                updateCollapsedText()
            } else {
                updateExpandedText()
            }
        } else {
            super.setText(originalText, bufferType)
        }
    }

    private fun updateCollapsedText() {
        var endIndex = lineEndIndex

        //find enough space to layout ELLIPSIZE
        val ellipsizeWidth = paint.measureText(ELLIPSIZE)
        var collapsedWidth = paint.measureText(originalText[endIndex].toString())
        while (collapsedWidth < ellipsizeWidth) {
            collapsedWidth += paint.measureText(originalText[--endIndex].toString())
        }

        var s = SpannableStringBuilder(originalText, 0, endIndex)
                .append(ELLIPSIZE)
                .append(LINE_END)
                .append(collapsedText)

        s.setSpan(ToggleSpan(), s.length - collapsedText.length, s.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        super.setText(s, bufferType)
    }

    private fun updateExpandedText() {
        if (enableCollapsedText) {
            var s = SpannableStringBuilder(originalText, 0, originalText.length)
                    .append(LINE_END)
                    .append(expandedText)

            s.setSpan(ToggleSpan(), s.length - expandedText.length, s.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            super.setText(s, bufferType)
        } else {
            super.setText(originalText, bufferType)
        }
    }

    inner class ToggleSpan : ClickableSpan() {
        override fun onClick(widget: View?) {
            setExpanded(!readMore)
        }

        override fun updateDrawState(ds: TextPaint) {
            ds.color = trimTextColor
            ds.isUnderlineText = false
        }
    }

    fun setExpanded(readMore: Boolean) {
        this.readMore = readMore
        //fixme
        tag = readMore
        setTextInternal()
    }
}