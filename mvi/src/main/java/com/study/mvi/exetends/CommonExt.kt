package com.study.mvi.exetends

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.study.mvi.utils.ObjectStore
import com.study.mvi.utils.ScreenUtil
import timber.log.Timber

/**
 * Toast扩展函数
 */
fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(ObjectStore.context!!, message, duration).show()
}

fun Context.log(message: String) {
    Timber.tag("TTT").e(message)
}

fun log(message: String) {
    Timber.tag("Tag").e(message)
}

fun Number.dp2px(): Int? {
    return ObjectStore.context!!.let {
        ScreenUtil.dp2px(it, toFloat())
    }
}

fun Number.px2dp(): Int {
    return ScreenUtil.px2dp(ObjectStore.context!!, toFloat())
}

fun Number.sp2px(): Int {
    return ScreenUtil.sp2px(ObjectStore.context!!, toFloat())
}

fun Number.px2sp(): Int {
    return ScreenUtil.px2sp(ObjectStore.context!!, toFloat())
}

fun View?.visible() {
    if (this?.visibility != View.VISIBLE) {
        this?.visibility = View.VISIBLE
    }
}

fun View?.invisible() {
    if (this?.visibility != View.INVISIBLE) {
        this?.visibility = View.INVISIBLE
    }
}

fun View?.gone() {
    if (this?.visibility != View.GONE) {
        this?.visibility = View.GONE
    }
}

fun Activity.showToast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, duration).show()
}

fun Activity.showToast(@StringRes msg: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, msg, duration).show()
}

fun Fragment.showToast(msg: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(requireContext(), msg, duration).show()
}

fun Fragment.showToast(@StringRes message: Int, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(requireContext(), message, duration).show()
}



