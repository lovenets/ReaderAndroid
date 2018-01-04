package net.codysehl.www.reader.ReduxLike

import android.util.Log
import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.KodeinAware
import com.github.salomonbrys.kodein.conf.ConfigurableKodein
import com.github.salomonbrys.kodein.conf.KodeinGlobalAware
import com.github.salomonbrys.kodein.conf.global
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.with
import io.reactivex.rxkotlin.subscribeBy
import net.codysehl.www.reader.Model.Book
import net.codysehl.www.reader.Repository.BookSearchRepository
import java.util.logging.Logger

class ActionCreator(override val kodein: ConfigurableKodein) : KodeinAware {
    private val store: Store<ApplicationState> = instance()
    private val bookSearchRepo: BookSearchRepository = with(kodein).instance()

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
}