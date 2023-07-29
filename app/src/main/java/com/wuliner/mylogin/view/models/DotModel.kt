package com.wuliner.mylogin.view.models

import android.graphics.RectF
import android.util.Log

data class DotModel(
    val cx: Float, //记录点的横坐标
    val cy: Float, //记录点的纵坐标
    val num: Int, //记录点对应的编号
    val radius: Float, //记录点的半径
    var state: DotState = DotState.Normal //记录点的状态
    ) {
    //判断是否在包含一点
    fun  containsPaint(x: Float, y: Float): Boolean {
        val rect = RectF(cx - radius, cy-radius, cx  + radius, cy+radius)
        return rect.contains(x, y)
    }
}

enum class DotState {
    Normal, Selected, Error
}