package com.wuliner.mylogin.tool

import android.animation.ObjectAnimator
import android.view.View

object AnimTools {
    //左右摆动动画
    fun startSwingAnim(
        view: View,
        scope: Int = 10,
        time: Long = 500
    ) {
        val distance = view.dp2px(scope).toFloat()
        ObjectAnimator.ofFloat(view, "translationX", 0f, -distance, 2*distance, -distance).apply {
            duration = time
            repeatCount = 3
            start()
        }
    }
}