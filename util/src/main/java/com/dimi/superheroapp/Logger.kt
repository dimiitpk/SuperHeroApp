package com.dimi.superheroapp

import com.dimi.superheroapp.Constants.DEBUG
import com.dimi.superheroapp.Constants.DEBUG_TAG
import com.dimi.superheroapp.Constants.ERROR_TAG

object Logger {
    fun printLogD(className: String?, message: String) {
        if (DEBUG) {
            println("$DEBUG_TAG -> $className: $message")
        }
    }

    fun printLogE(className: String?, message: String) {
        if (DEBUG) {
            println("$ERROR_TAG -> $className: $message")
        }
    }
}