package com.yyide.chatim_pro.utils

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.yyide.chatim_pro.BaseApplication

/**
 *
 * @author shenzhibin
 * @date 2022/3/22 18:08
 * @description kt的特性帮助开发更加简易
 */

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.INVISIBLE
}

fun View.remove() {
    this.visibility = View.GONE
}


// Toash extensions
fun Context.showShotToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.showLongToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

// Snackbar Extensions
fun View.showShotSnackBar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_SHORT).show()
}

fun View.showLongSnackBar(message: String) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG).show()
}

fun View.snackBarWithAction(
    message: String, actionLabel: String,
    block: () -> Unit
) {
    Snackbar.make(this, message, Snackbar.LENGTH_LONG)
        .setAction(actionLabel) {
            block()
        }.show()
}

//Extensions
fun Int.asColor() = ContextCompat.getColor(BaseApplication.getInstance(), this)
fun Int.asDrawable() = ContextCompat.getDrawable(BaseApplication.getInstance(), this)

