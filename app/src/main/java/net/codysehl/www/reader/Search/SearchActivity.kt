package net.codysehl.www.reader.Search

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import com.github.salomonbrys.kodein.*
import com.github.salomonbrys.kodein.conf.*
import com.jakewharton.rxbinding2.view.RxView
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.Observable

class SearchActivity: Activity(), SearchPresenter.View {
    override lateinit var searchTermChanged: Observable<String>
    override lateinit var searchTermSubmitted: Observable<Any>
    private val injector = KodeinInjector()

    val presenter: SearchPresenter by injector.with(Kodein.global).instance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        injector.inject(Kodein.global)

        val linearLayout = LinearLayout(this)

        linearLayout.orientation = LinearLayout.VERTICAL

        val searchBar = EditText(this)
        linearLayout.addView(searchBar)

        val searchSubmitButton = Button(this)
        searchSubmitButton.text = "Submit"
        linearLayout.addView(searchSubmitButton)

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
        println("rendering $props")
    }
}
