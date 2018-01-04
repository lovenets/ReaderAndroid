package net.codysehl.www.reader.Search

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import android.widget.TextView
import net.codysehl.www.reader.R

class BookListAdapter(var books: List<SearchPresenter.Props.Book>): RecyclerView.Adapter<BookListAdapter.BookListItemViewHolder>() {
    override fun getItemCount(): Int = books.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookListItemViewHolder {
        val view = BookListItemView(parent.context)
        return BookListItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookListItemViewHolder, position: Int) {
        val book = books[position]

        holder.bind(book)
    }

    class BookListItemViewHolder(val bookListItemView: BookListItemView): RecyclerView.ViewHolder(bookListItemView) {
        fun bind(book: SearchPresenter.Props.Book) {
            this.bookListItemView.title.text = book.title
            this.bookListItemView.author.text = book.author
        }
    }

    class BookListItemView(context: Context): LinearLayout(context) {
        val title: TextView
        val author: TextView

        init {
            title = TextView(context)
            addView(title)

            author = TextView(context)
            addView(author)
        }
    }
}