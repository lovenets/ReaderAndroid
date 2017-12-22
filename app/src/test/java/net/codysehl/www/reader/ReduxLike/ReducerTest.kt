package net.codysehl.www.reader.ReduxLike

import org.junit.Test

import org.junit.Assert.*

class ReducerTest {
    @Test
    fun reduce_SearchTextEntered() {
        val searchText = "David Foster Wallace"

        assertEquals(
                ApplicationState(searchText),
                Reducer.reduce(ApplicationState(), Action.SearchTextEntered(searchText))
        )
    }

}