package com.sunguk.itbooklibrary.util

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

fun ViewGroup.createView(layoutId: Int): View =
    LayoutInflater.from(context).inflate(layoutId, this, false)