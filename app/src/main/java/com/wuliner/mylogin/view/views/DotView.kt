package com.wuliner.mylogin.view.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.wuliner.mylogin.R
import com.wuliner.mylogin.tool.dp2px
import com.wuliner.mylogin.view.models.DotModel
import kotlin.math.min

class DotView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    //预设圆的半径
    private var mRadius = dp2px(40)
    private var mSpace = dp2px(40)
    private var mCenterRadius = 0f
    private var mStart_Cx = 0f//记录第一个点的起始坐标点
    private val mStart_Cy = 0f//记录第一个点的起始坐标点
    private val mDotPaint: Paint by lazy {
        Paint().apply {
            isAntiAlias = true
            color = resources.getColor(R.color.dot_bg_color, null)
            style = Paint.Style.FILL
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        var mWidth = 0
        var mHeight = 0
        //设置宽度
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        mWidth =  when (widthMode) {
            MeasureSpec.EXACTLY -> widthSize
            else -> 6*mRadius + 4*mSpace
        }
        //设置高度
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        mHeight =  when (heightMode) {
            MeasureSpec.EXACTLY -> heightSize
            else -> 6*mRadius + 4*mSpace
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //计算半径
        mRadius = (min(height, width) - 4 * mSpace) / 6
        mCenterRadius = mRadius.toFloat() / 6

        mStart_Cx = (width - min(height, width))/2 + mSpace + mRadius.toFloat()
        mStart_Cx = (height - min(height, width))/2 + mSpace + mRadius.toFloat()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawNineDot(canvas)
    }

    //绘制9个点
    private var mDotModel = arrayListOf<DotModel>()//保存九个点
    private fun drawNineDot(canvas: Canvas?) {
        val index = 1
        for (i in 0 until 3) {
            for (j in 0 until 3) {
                val cx = mStart_Cx + j * (2* mRadius + mSpace)
                val cy = mStart_Cy + i * (2* mRadius + mSpace)
                canvas?.drawCircle(cx, cy, mRadius.toFloat(), mDotPaint)
            }
        }
    }
}