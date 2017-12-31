package net.codysehl.www.reader.ReduxLike

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.KodeinAware
import com.github.salomonbrys.kodein.conf.ConfigurableKodein
import com.github.salomonbrys.kodein.conf.KodeinGlobalAware
import com.github.salomonbrys.kodein.conf.global
import com.github.salomonbrys.kodein.instance
import net.codysehl.www.reader.Model.Book
import net.codysehl.www.reader.Repository.BookSearchRepository

class ActionCreator(override val kodein: ConfigurableKodein) : KodeinAware {
    private val store: Store<ApplicationState> = instance()
    private val bookSearchRepo: BookSearchRepository = instance()

    fun searchTermChanged(text: String) = store.dispatch(Action.SearchTermChanged(text))
    fun searchSubmitted() {
        bookSearchRepo.search(store.state.searchText)
                .onSuccess { books: List<Book> ->
                    store.dispatch(Action.SearchCompletedWithSuccess(books))
                }.onFailure {
                    store.dispatch(Action.SearchCompletedWithFailure())
                }

        store.dispatch(Action.SearchSubmitted())
    }
}