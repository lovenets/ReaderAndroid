package net.codysehl.www.reader.ReduxLike

sealed class Action {
    data class SearchTextEntered(val text: String): Action()
}
