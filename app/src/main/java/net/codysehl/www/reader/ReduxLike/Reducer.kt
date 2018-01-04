package net.codysehl.www.reader.ReduxLike

import android.util.Log

object Reducer {
    fun reduce(state: ApplicationState, action: Action): ApplicationState {
        Log.d("#Lifecycle#", "Reducing: $action")
        return when (action) {
            is Action.SearchTermChanged -> reduceSearchTermChangedAction(state, action)
            is Action.SearchSubmitted -> reduceSearchSubmittedAction(state, action)
            is Action.SearchCompletedWithSuccess -> reduceSearchCompletedWithSuccessAction(state, action)
            is Action.SearchCompletedWithFailure -> reduceSearchCompletedWithFailureAction(state, action)
        }
    }

    private fun reduceSearchTermChangedAction(state: ApplicationState, action: Action.SearchTermChanged): ApplicationState {
        return state.copy(searchText = action.text)
    }

    private fun reduceSearchSubmittedAction(state: ApplicationState, action: Action.SearchSubmitted): ApplicationState {
        return state.copy(searchPending = true)
    }

    private fun reduceSearchCompletedWithSuccessAction(state: ApplicationState, action: Action.SearchCompletedWithSuccess): ApplicationState {
        return state.copy(searchPending = false, books = action.books)
    }

    private fun reduceSearchCompletedWithFailureAction(state: ApplicationState, action: Action.SearchCompletedWithFailure): ApplicationState {
        return state.copy(searchPending = false)
    }
}
