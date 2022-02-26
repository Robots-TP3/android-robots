package com.example.frontend_robots.ui

import androidx.lifecycle.ViewModel

class ShareViewModel : ViewModel() {

    private var isFragmentWithNetwork = false
    var shouldClearInputsWhenBackPressed = true

    fun setFragmentWithNetwork(haveNetwork: Boolean) { isFragmentWithNetwork = haveNetwork }

}
