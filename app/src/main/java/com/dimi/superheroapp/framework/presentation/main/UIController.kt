package com.dimi.superheroapp.framework.presentation.main

import com.dimi.superheroapp.business.domain.state.Response
import com.dimi.superheroapp.business.domain.state.StateMessageCallback

interface UIController {

    fun displayProgressBar(isDisplayed: Boolean)

    fun hideSoftKeyboard()

    fun expandAppBar()

    fun changeThemeMode()

    fun onResponseReceived(
        response: Response,
        stateMessageCallback: StateMessageCallback
    )
}


















