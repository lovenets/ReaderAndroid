package net.codysehl.www.reader.Search

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.*
import com.github.salomonbrys.kodein.*
import com.github.salomonbrys.kodein.conf.*
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable

class SearchActivity : Activity(), SearchPresenter.View {
    override lateinit var searchTermChanged: Observable<String>
    override lateinit var searchTermSubmitted: Observable<Any>
    private val injector = KodeinInjector()

    private val presenter: SearchPresenter by injector.with(Kodein.global).instance()

    private lateinit var searchBar: EditText
    private lateinit var searchSubmitButton: Button
    private lateinit var progressIndicator: ProgressBar
    private lateinit var bookListAdapter: BookListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injector.inject(Kodein.global)

        val linearLayout = LinearLayout(this)

        linearLayout.orientation = LinearLayout.VERTICAL

        searchBar = EditText(this)
        linearLayout.addView(searchBar)

        searchSubmitButton = Button(this)
        searchSubmitButton.text = "Submit"
        linearLayout.addView(searchSubmitButton)

        progressIndicator = ProgressBar(this)
        progressIndicator.visibility = View.GONE
        linearLayout.addView(progressIndicator)

        bookListAdapter = BookListAdapter(listOf())
        val recyclerView = RecyclerView(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = bookListAdapter
        linearLayout.addView(recyclerView)

        setContentView(linearLayout)

        searchTermChanged = RxTextView.textChanges(searchBar).map { it.toString() }
        searchTermSubmitted = RxView.clicks(searchSubmitButton)
        presenter.onViewReady(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

    override fun render(props: SearchPresenter.Props) {
        progressIndicator.visibility = if (props.showLoadingSpinner) {
            View.VISIBLE
        } else {
            View.GONE
        }

        searchBar.isEnabled = !props.disableSearchBar
        searchSubmitButton.isEnabled = !props.disableSearchSubmitButton

        bookListAdapter.books = props.books
        bookListAdapter.notifyDataSetChanged()

        Log.d("Lifcycle", "Rendering $props")
    }
}
