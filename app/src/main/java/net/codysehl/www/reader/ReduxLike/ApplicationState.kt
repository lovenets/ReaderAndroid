package net.codysehl.www.reader.ReduxLike

import net.codysehl.www.reader.Model.Book

data class ApplicationState(
        val searchText: String = "",
        val searchPending: Boolean = false,
        val books: List<Book> = listOf()
)