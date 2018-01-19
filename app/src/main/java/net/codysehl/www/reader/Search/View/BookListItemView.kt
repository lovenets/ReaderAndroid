package net.codysehl.www.reader.Search.View

import android.content.Context
import android.widget.LinearLayout
import android.widget.TextView

class BookListItemView(context: Context): LinearLayout(context) {

    data class Props(
            val title: String,
            val author: String
    )

    val title: TextView
    val author: TextView

    init {
        title = TextView(context)
        addView(title)

        author = TextView(context)
        addView(author)
    }

    fun update(props: Props) {
        title.text = props.title
        author.text = props.author
    }
}