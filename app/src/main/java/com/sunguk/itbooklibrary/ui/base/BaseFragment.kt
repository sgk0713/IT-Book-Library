package com.sunguk.itbooklibrary.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VB : ViewBinding> : Fragment() {

    private var _binding: VB? = null
    protected val binding: VB
        get() = _binding as VB

    abstract val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> VB

    @Deprecated(
        message = "override onCreateViewCustomized() instead",
        level = DeprecationLevel.ERROR
    )
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = bindingInflater.invoke(inflater, container, false)
        return binding.root.apply {
            onCreateViewCustomized(savedInstanceState)
        }
    }

    @Deprecated(
        message = "override onViewCreatedCustomized() instead",
        level = DeprecationLevel.ERROR
    )
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreatedCustomized(view, savedInstanceState)
    }

    @Deprecated(
        message = "override onDestroyViewCustomized() instead",
        level = DeprecationLevel.ERROR
    )
    override fun onDestroyView() {
        super.onDestroyView()
        onDestroyViewCustomized()
        _binding = null
    }

    protected open fun onViewCreatedCustomized(view: View, savedInstanceState: Bundle?) = Unit
    protected open fun onCreateViewCustomized(savedInstanceState: Bundle?) = Unit
    protected open fun onDestroyViewCustomized() = Unit
    abstract fun initUi()
    abstract fun initBinding()
}
