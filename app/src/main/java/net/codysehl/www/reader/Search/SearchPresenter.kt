package net.codysehl.www.reader.Search

import com.github.salomonbrys.kodein.KodeinAware
import com.github.salomonbrys.kodein.conf.ConfigurableKodein
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.with
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import net.codysehl.www.reader.ReduxLike.ApplicationState
import net.codysehl.www.reader.ReduxLike.ActionCreator
import net.codysehl.www.reader.KodeinTag
import net.codysehl.www.reader.Model.Book
import net.codysehl.www.reader.Search.SearchPresenter.Props.Book.Companion.fromModel

class SearchPresenter(override val kodein: ConfigurableKodein) : KodeinAware {

    private val applicationState: Observable<ApplicationState> = instance()
    private val actionCreator: ActionCreator = with(kodein).instance()
    private val mainThreadScheduler: Scheduler = instance(KodeinTag.MAIN_THREAD)
    private var disposables: List<Disposable> = listOf()

    fun onViewReady(view: View) {
        disposables = listOf(
                applicationState
                        .observeOn(mainThreadScheduler)
                        .map { Props.fromState(it) }
                        .doOnNext { view.render(it) }
                        .subscribe(),

                view.searchTermChanged
                        .doOnNext {
                            actionCreator.searchTermChanged(it)
                        }
                        .subscribe(),

                view.searchTermSubmitted
                        .doOnNext {
                            actionCreator.searchSubmitted()
                        }
                        .subscribe()
        )
    }

    fun onDestroy() {
        disposables.forEach(Disposable::dispose)
    }

    // Interface

    interface View {
        val searchTermChanged: Observable<String>
        val searchTermSubmitted: Observable<Any>

        fun render(props: Props)
    }

    data class Props(
            val searchText: String,
            val showLoadingSpinner: Boolean,
            val disableSearchBar: Boolean,
            val disableSearchSubmitButton: Boolean,
            val books: List<Book>
    ) {
        data class Book(val title: String, val author: String) {
            companion object {
                fun fromModel(book: net.codysehl.www.reader.Model.Book): Book {
                    return Book(book.title, book.author)
                }
            }
        }

        companion object {
            fun fromState(state: ApplicationState): Props {
                return Props(
                        searchText = state.searchText,
                        showLoadingSpinner = state.searchPending,
                        disableSearchBar = state.searchPending,
                        disableSearchSubmitButton = state.searchPending,
                        books = state.books.map { Book.fromModel(it) }
                )
            }
        }
    }
}