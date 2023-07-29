package com.wuliner.mylogin.tool

import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.wuliner.mylogin.R

fun Fragment.navigateTo(
    target: Fragment,
    enterAnim: Int = R.anim.enter_from_right,
    exitAnim: Int = R.anim.exit_to_left_,
    popEnter: Int = R.anim.pop_enter_from_left,
    popExit: Int = R.anim.pop_exit_to_right,
    addToStack: Boolean = true,
) {
    parentFragmentManager.commit {
        setCustomAnimations(enterAnim, exitAnim, popEnter, popExit)
        replace(R.id.fragmentContainerView, target)
        setReorderingAllowed(true)
        if (addToStack) {
            addToBackStack(null)
        }
    }
}

fun View.dp2px(dp: Int): Int {
    return (this.resources.displayMetrics.density*dp).toInt()
}