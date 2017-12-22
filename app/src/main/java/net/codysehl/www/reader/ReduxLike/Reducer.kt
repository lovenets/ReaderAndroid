package net.codysehl.www.reader.ReduxLike

object Reducer {
    fun reduce(state: ApplicationState, action: Action): ApplicationState =
            when(action) {
                is Action.SearchTextEntered -> reduceSearchTextEnteredAction(state, action)
            }

    private fun reduceSearchTextEnteredAction(state: ApplicationState, action: Action.SearchTextEntered): ApplicationState {
//        return state.copy(searchText = action.text)
        return state
    }
}
