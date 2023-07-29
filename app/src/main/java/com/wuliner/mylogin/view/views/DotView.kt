package com.wuliner.mylogin.view.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.Style
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.wuliner.mylogin.R
import com.wuliner.mylogin.tool.dp2px
import com.wuliner.mylogin.view.models.DotModel
import com.wuliner.mylogin.view.models.DotState
import java.lang.StringBuilder
import kotlin.math.max
import kotlin.math.min

class DotView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    //预设圆的半径
    private var mRadius = dp2px(40)
    private var mSpace = dp2px(40)
    private var mCenterRadius = 0f
    private var mStart_Cx = 0f//记录第一个点的起始坐标点
    private var mStart_Cy = 0f//记录第一个点的起始坐标点
    private val mDotPaint: Paint by lazy {
        Paint().apply {
            isAntiAlias = true
            color = resources.getColor(R.color.dot_bg_color, null)
            style = Paint.Style.FILL
        }
    }
    private var lastSelectedDot: DotModel? = null//记录上一个点
    private val mCenterDotPaint: Paint by lazy {
        Paint().apply {
            isAntiAlias = true
            color = Color.WHITE
            style = Style.FILL
        }
    }
    private val mDotSelectedPaint: Paint by lazy {
        Paint().apply {
            isAntiAlias = true
            color = resources.getColor(R.color.light_blue, null)
            style = Style.STROKE
            strokeWidth = dp2px(2).toFloat()
        }
    }
    private val passwordBuidler = StringBuilder()
    private val lightedLine = arrayListOf<Int>()


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
        setMeasuredDimension(mWidth, mHeight)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        //计算半径
        mRadius = (min(height, width) - 4 * mSpace) / 6
        mCenterRadius = mRadius.toFloat() / 6

        mStart_Cx = (width - min(height, width))/2 + mSpace + mRadius.toFloat()
        mStart_Cy = (height - min(height, width))/2 + mSpace + mRadius.toFloat()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawNineDot(canvas)
        drawLine(canvas)
    }



    //绘制9个点
    private var mDotModels = arrayListOf<DotModel>()//保存九个点
    private fun drawNineDot(canvas: Canvas?) {
        var index = 1
        for (i in 0 until 3) {
            for (j in 0 until 3) {
                val cx = mStart_Cx + j * (2* mRadius + mSpace)
                val cy = mStart_Cy + i * (2* mRadius + mSpace)
                canvas?.drawCircle(cx, cy, mRadius.toFloat(), mDotPaint)
                if (index > mDotModels.size) {
                    mDotModels.add(DotModel(cx, cy, index, mRadius.toFloat() ))
                } else {
                    //根据状态绘制
                    when (mDotModels[index-1].state) {
                        DotState.Selected -> {
                            mDotSelectedPaint.color = resources.getColor(R.color.light_blue, null)
                            canvas?.drawCircle(cx, cy, mRadius.toFloat(), mDotSelectedPaint)
                            mCenterDotPaint.color = resources.getColor(R.color.light_blue, null)
                            canvas?.drawCircle(cx, cy, mCenterRadius, mCenterDotPaint)

                        }
                        DotState.Error -> {
                            mDotSelectedPaint.color = resources.getColor(R.color.red, null)
                            canvas?.drawCircle(cx, cy, mRadius.toFloat(), mDotSelectedPaint)
                            mCenterDotPaint.color = resources.getColor(R.color.red, null)
                            canvas?.drawCircle(cx, cy, mCenterRadius, mCenterDotPaint)

                        }
                        DotState.Normal -> {}
                    }
                }
                index++
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> { touchBegin(event.x, event.y) }
            MotionEvent.ACTION_MOVE -> { touchBegin(event.x, event.y) }
            MotionEvent.ACTION_UP -> {touchEnd() }
        }
        return true
    }
    private var mPath = Path() //线的路径
    private var mMovePath = Path() //移动的线的路径
    private val mLinePaint: Paint by lazy {
        Paint().apply {
            color = resources.getColor(R.color.light_blue, null)
            strokeWidth = dp2px(2).toFloat()
            style = Style.STROKE
        }
    }
    private var mBackupPath: Path? = null
    private var mBackupMovePath: Path? = null
    private var mBackupDots: ArrayList<DotModel>? = null

    private fun touchBegin(x: Float, y: Float) {
        val dot = isInDot(x, y)
        if (dot != null) {
            if (lastSelectedDot == null) {
                lastSelectedDot = dot
                mPath.moveTo(dot.cx, dot.cy)
                passwordBuidler.append(dot.num)
            } else {
                if (lastSelectedDot != dot) {
                    val lineNum = getLineNum(dot)
                    if (!checkLineExist(lineNum)) {
                        mPath.lineTo(dot.cx, dot.cy)
                        lightedLine.add(getLineNum(dot))
                        lastSelectedDot = dot
                        mMovePath.reset()
                        mMovePath.moveTo(lastSelectedDot!!.cx, lastSelectedDot!!.cy)
                        passwordBuidler.append(dot.num)
                    }
                }
            }
            dot.state = DotState.Selected
        } else {
            addMoveLine(x, y)
        }
        invalidate()
    }
    //记录回调事件
    private  var mCallback: ((DotView, String) -> Unit)? = null
    private fun touchEnd() {
        //获取密码
        val password = passwordBuidler.toString()
        mBackupMovePath = Path(mMovePath)
        mBackupPath = Path(mPath)
        mBackupDots = ArrayList(mDotModels)
        clear()
        mCallback?.let { it(this, password) }
    }

    /**显示错误状态*/
    fun showError() {
        mPath = mBackupPath ?: Path()
        mMovePath = mBackupMovePath ?: Path()
        mDotModels = mBackupDots?.let { ArrayList(it) }!!
        mDotModels.forEach {
            if (it.state == DotState.Selected) {
                it.state = DotState.Error
            }
        }
        mLinePaint.color = resources.getColor(R.color.red, null)
        invalidate()
        postDelayed({
            mLinePaint.color = resources.getColor(R.color.light_blue, null)
            mBackupPath = null
            mBackupDots = null
            mBackupMovePath = null
            clear()
        }, 500)
    }

    /**监听密码绘制结束*/
    fun addDrawFinishedListerner(callback: (DotView, String) -> Unit) {
        mCallback = callback
    }

    private fun drawLine(canvas: Canvas?) {
        if (mPath.isEmpty) return
        canvas?.drawPath(mPath, mLinePaint)
        if (mMovePath.isEmpty) return
        canvas?.drawPath(mMovePath, mLinePaint)
    }

    private fun isInDot(tX: Float, tY: Float): DotModel? {
        mDotModels.forEach {
            if (it.containsPaint(tX, tY)) {
                return it
            }
        }
        return null
    }

    private fun clear() {
        passwordBuidler.clear()
        mPath.reset()
        mMovePath.reset()
        mDotModels.clear()
        lastSelectedDot = null
        lightedLine.clear()
        invalidate()
    }


    private fun addMoveLine(x: Float, y: Float) {
        mMovePath.reset()
        mMovePath.moveTo(lastSelectedDot!!.cx, lastSelectedDot!!.cy)
        mMovePath.lineTo(x, y)
    }

    fun getLineNum(dot: DotModel): Int {
        val min = min(lastSelectedDot!!.num, dot.num)
        val max = max(lastSelectedDot!!.num, dot.num)
        return min*10 + max
    }

    fun checkLineExist(line: Int): Boolean {
        lightedLine.forEach {
            if (it == line) return true
        }
        return false
    }
}