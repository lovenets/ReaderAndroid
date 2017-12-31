package net.codysehl.www.reader.ReduxLike

import com.github.salomonbrys.kodein.*
import com.github.salomonbrys.kodein.conf.global

sealed class Action {
    data class SearchTermChanged(val text: String): Action()
    data class SearchSubmitted(val unit: Unit = Unit): Action()
}

class ActionCreator {
    private val store: Store<ApplicationState> = Kodein.global.instance()

    fun searchTermChanged(text: String) = store.dispatch(Action.SearchTermChanged(text))
    fun searchSubmitted() = store.dispatch(Action.SearchSubmitted())
}
