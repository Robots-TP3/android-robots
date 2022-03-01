package com.example.frontend_robots.utils

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.frontend_robots.ui.ShareViewModel

abstract class BaseFragment(@LayoutRes layout: Int) : Fragment(layout) {

    protected open val isNetworkRequired = false
    protected val sharedViewModel by activityViewModels<ShareViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        sharedViewModel.setFragmentWithNetwork(isNetworkRequired)
        sharedViewModel.shouldClearInputsWhenBackPressed = shouldClearInputsWhenBackPressed()
    }

    protected open fun shouldClearInputsWhenBackPressed() = true
}