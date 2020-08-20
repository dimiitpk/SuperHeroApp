package com.dimi.superheroapp.presentation.main

import com.dimi.superheroapp.business.domain.state.Response
import com.dimi.superheroapp.business.domain.state.StateMessageCallback

interface UIController {

    fun displayProgressBar(isDisplayed: Boolean)

    fun hideSoftKeyboard()

    fun expandAppBar()

    fun changeThemeMode()

    fun isDeviceTabletInLandscapeMode(): Boolean

    fun onResponseReceived(
        response: Response,
        stateMessageCallback: StateMessageCallback
    )
}


















