package net.codysehl.www.reader.ReduxLike

import android.util.Log
import io.reactivex.rxkotlin.subscribeBy
import net.codysehl.www.reader.Repository.BookSearchRepository
import net.codysehl.www.reader.Repository.GoogleBookSearchRepository

class ActionCreator(
        private val store: Store<ApplicationState>,
        private val bookSearchRepo: BookSearchRepository
) {
    fun searchTermChanged(text: String) = store.dispatch(Action.SearchTermChanged(text))
    fun searchSubmitted() {
        val term = store.state.searchText
        store.dispatch(Action.SearchSubmitted())

        bookSearchRepo.search(term)
                .subscribeBy({ error ->
                    Log.e("Lifecycle", error.toString())
                    store.dispatch(Action.SearchCompletedWithFailure(error.message ?: "Failed to fetch search results for $term"))
                }, {  }, { books ->
                    store.dispatch(Action.SearchCompletedWithSuccess(books))
                })
    }

    companion object {
        fun create(): ActionCreator = ActionCreator(
                Store.singleton,
                GoogleBookSearchRepository.create()
        )
    }
}