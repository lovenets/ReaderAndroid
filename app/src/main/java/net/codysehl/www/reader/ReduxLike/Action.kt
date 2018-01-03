package net.codysehl.www.reader.ReduxLike

import net.codysehl.www.reader.Model.Book

sealed class Action {
    data class SearchTermChanged(val text: String): Action()

    data class SearchSubmitted(val unit: Unit = Unit): Action()
    data class SearchCompletedWithSuccess(val books: List<Book>): Action()
    data class SearchCompletedWithFailure(val error: String): Action()
}