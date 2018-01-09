package net.codysehl.www.reader.Search.View

import android.content.Context
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ProgressBar
import codysehl.net.RecyclerViewListAdapter.RecyclerViewListAdapter
import net.codysehl.www.reader.Search.SearchPresenter

class SearchView(context: Context): LinearLayout(context) {

    var searchBar: EditText
    var searchSubmitButton: Button
    var progressIndicator: ProgressBar
    var bookListAdapter: RecyclerViewListAdapter<BookListItemView, BookListItemView.Props>

    init {
        orientation = LinearLayout.VERTICAL

        searchBar = EditText(context)
        addView(searchBar)

        searchSubmitButton = Button(context)
        searchSubmitButton.text = "Submit"
        addView(searchSubmitButton)

        progressIndicator = ProgressBar(context)
        progressIndicator.visibility = View.GONE
        addView(progressIndicator)

        bookListAdapter = RecyclerViewListAdapter.create({ BookListItemView(context) }, { view, props: BookListItemView.Props -> view.update(props) })
        val recyclerView = RecyclerView(context)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = bookListAdapter
        addView(recyclerView)
    }

    fun update(props: SearchPresenter.Props) {
        progressIndicator.visibility = if (props.showLoadingSpinner) {
            View.VISIBLE
        } else {
            View.GONE
        }

        searchBar.isEnabled = !props.disableSearchBar
        searchSubmitButton.isEnabled = !props.disableSearchSubmitButton

        bookListAdapter.items = props.books.map { BookListItemView.Props(it.title, it.author) }
        bookListAdapter.notifyDataSetChanged()
    }
}