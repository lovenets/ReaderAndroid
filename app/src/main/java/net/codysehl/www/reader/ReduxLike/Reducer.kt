package net.codysehl.www.reader.ReduxLike

object Reducer {
    fun reduce(state: ApplicationState, action: Action): ApplicationState {
        println("Reducing: $action")
        return when (action) {
            is Action.SearchTermChanged -> reduceSearchTermChangedAction(state, action)
            is Action.SearchSubmitted -> reduceSearchSubmittedAction(state, action)
        }
    }

    private fun reduceSearchTermChangedAction(state: ApplicationState, action: Action.SearchTermChanged): ApplicationState {
        return state.copy(searchText = action.text)
    }

    private fun reduceSearchSubmittedAction(state: ApplicationState, action: Action.SearchSubmitted): ApplicationState {
        return state
    }
}
