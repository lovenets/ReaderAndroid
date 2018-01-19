package net.codysehl.www.reader.Search

import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.disposables.Disposable
import net.codysehl.www.reader.ReduxLike.ActionCreator
import net.codysehl.www.reader.ReduxLike.ApplicationState
import net.codysehl.www.reader.ReduxLike.Store
import net.codysehl.www.reader.SchedulerFactory

class SearchPresenter(
        private val applicationState: Observable<ApplicationState>,
        private val actionCreator: ActionCreator,
        private val scheduler: Scheduler
) {

    private var disposables: List<Disposable> = listOf()

    fun onViewReady(view: View) {
        disposables = listOf(
                applicationState
                        .observeOn(scheduler)
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

    companion object {
        fun create(): SearchPresenter {
            return SearchPresenter(
                    Store.singleton.observable,
                    ActionCreator.create(),
                    SchedulerFactory.createMainThreadScheduler()
            )
        }
    }
}