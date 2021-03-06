package com.dimi.superheroapp.presentation.main.state

import com.dimi.superheroapp.business.domain.state.StateEvent
import com.dimi.superheroapp.business.domain.state.StateMessage


sealed class MainStateEvent: StateEvent {

    class SearchHeroes(
        val clearLayoutManagerState: Boolean = true
    ): MainStateEvent() {

        override fun errorInfo(): String {
            return "Error searching superheroes."
        }

        override fun eventName(): String {
            return "SearchSuperHeroes"
        }

        override fun shouldDisplayProgressBar() = true
    }

    class SearchHeroesFromCacheOnly : MainStateEvent() {
        override fun errorInfo(): String {
            return "Error retrieving superheroes from cache."
        }

        override fun eventName(): String {
            return "SearchHeroesFromCacheOnly"
        }

        override fun shouldDisplayProgressBar() = false
    }

    class CreateStateMessageEvent(
        val stateMessage: StateMessage
    ): MainStateEvent(){

        override fun errorInfo(): String {
            return "Error creating a new state message."
        }

        override fun eventName(): String {
            return "CreateStateMessageEvent"
        }

        override fun shouldDisplayProgressBar() = false
    }

}




















