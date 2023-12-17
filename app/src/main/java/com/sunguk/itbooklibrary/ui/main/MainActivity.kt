package com.sunguk.itbooklibrary.ui.main

import android.view.LayoutInflater
import com.sunguk.itbooklibrary.databinding.ActivityMainBinding
import com.sunguk.itbooklibrary.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override val bindingInflater: (LayoutInflater) -> ActivityMainBinding =
        ActivityMainBinding::inflate
}