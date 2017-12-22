package net.codysehl.www.reader.Search

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.KodeinAware
import com.github.salomonbrys.kodein.instance
import io.reactivex.Observable
import io.reactivex.disposables.Disposable
import net.codysehl.www.reader.ReduxLike.ApplicationState
import net.codysehl.www.reader.ReduxLike.Action
import net.codysehl.www.reader.ReduxLike.Store

class SearchPresenter(override val kodein: Kodein) : KodeinAware {

    private val store: Store<ApplicationState> = instance()
    private var disposables: List<Disposable> = listOf()

    fun onViewReady(view: View) {
        disposables = listOf(
                store.observable
                        .map { Props.fromState(it) }
                        .doOnNext { view.render(it) }
                        .subscribe(),

                view.searchTermChanged
                        .doOnNext {
                            store.dispatch(Action.SearchTermChanged(it))
                        }
                        .subscribe(),

                view.searchTermSubmitted
                        .doOnNext {
                            store.dispatch(Action.SearchSubmitted())
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

    data class Props(val searchText: String) {
        companion object {
            fun fromState(state: ApplicationState): Props {
                return Props(searchText = state.searchText)
            }
        }
    }
}