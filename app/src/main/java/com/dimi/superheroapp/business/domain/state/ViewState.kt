package com.dimi.superheroapp.business.domain.state

interface ViewState {

    /**
     *  Use this function to set new incoming data in useCases
     *  Every ViewState need to override this method and write his own logic
     */
    fun setData(vararg any: Any?)
}