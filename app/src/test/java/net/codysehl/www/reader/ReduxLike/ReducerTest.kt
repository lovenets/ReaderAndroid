package net.codysehl.www.reader.ReduxLike

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
                ApplicationState(),
                Reducer.reduce(ApplicationState(), Action.SearchSubmitted())
        )
    }

}