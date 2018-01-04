package net.codysehl.www.reader.Search

import android.app.Activity
import android.os.Bundle
import android.util.Log
import com.github.salomonbrys.kodein.*
import com.github.salomonbrys.kodein.conf.*
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable
import net.codysehl.www.reader.Search.View.SearchView

class SearchActivity : Activity(), SearchPresenter.View {
    override lateinit var searchTermChanged: Observable<String>
    override lateinit var searchTermSubmitted: Observable<Any>
    private val injector = KodeinInjector()

    private lateinit var view: SearchView
    private val presenter: SearchPresenter by injector.with(Kodein.global).instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injector.inject(Kodein.global)

        view = SearchView(this)
        setContentView(view)

        searchTermChanged = RxTextView.textChanges(view.searchBar).map { it.toString() }
        searchTermSubmitted = RxView.clicks(view.searchSubmitButton)

        presenter.onViewReady(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun render(props: SearchPresenter.Props) {
        view.update(props)
        Log.d("Lifcycle", "Rendering $props")
    }
}
