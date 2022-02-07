package com.esq.androidpdfreader.utils

import android.content.Context
import android.widget.Toast

fun Context.shortToast(toastMsg: String){
    Toast.makeText(
        this,
        toastMsg,
        Toast.LENGTH_SHORT
    ).show()
}

fun Context.longToast(toastMsg: String){
    Toast.makeText(
        this,
        toastMsg,
        Toast.LENGTH_LONG
    ).show()
}