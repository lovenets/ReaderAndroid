package net.codysehl.www.reader.ReduxLike

import net.codysehl.www.reader.Model.Book
import org.junit.Test

import org.junit.Assert.*

class ReducerTest {
    @Test
    fun reduce_SearchTermChanged() {
        val searchText = "David Foster Wallace"

        assertEquals(
                ApplicationState(searchText),
                Reducer.reduce(ApplicationState(), Action.SearchTermChanged(searchText))
        )
    }

    @Test
    fun reduce_SearchSubmitted() {
        assertEquals(
                ApplicationState(searchPending = true),
                Reducer.reduce(ApplicationState(), Action.SearchSubmitted())
        )
    }

    @Test
    fun reduce_SearchCompletedWithSuccess() {
        val expectedBooks = listOf(Book("David Foster Wallace", "Ticket to the Fair"))
        assertEquals(
                ApplicationState(searchPending = false, books = expectedBooks ),
                Reducer.reduce(ApplicationState(searchPending = true), Action.SearchCompletedWithSuccess(expectedBooks))

        )
    }

    @Test
    fun reduce_SearchSubmittedWithFailure() {
        assertEquals(
                ApplicationState(searchPending = false),
                Reducer.reduce(ApplicationState(searchPending = true), Action.SearchCompletedWithFailure(""))
        )
    }

}