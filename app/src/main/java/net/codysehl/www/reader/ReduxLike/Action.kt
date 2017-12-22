package net.codysehl.www.reader.ReduxLike

sealed class Action {
    data class SearchTermChanged(val text: String): Action()
    data class SearchSubmitted(val unit: Unit = Unit): Action()
}
