package net.codysehl.www.reader.Search

import android.app.Activity
import android.os.Bundle
import android.view.View
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

        println("rendering $props")
    }
}
