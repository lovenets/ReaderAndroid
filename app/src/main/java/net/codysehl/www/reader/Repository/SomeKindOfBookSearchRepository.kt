package net.codysehl.www.reader.Repository

import android.util.Log
import com.github.salomonbrys.kodein.KodeinAware
import com.github.salomonbrys.kodein.conf.ConfigurableKodein
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.with
import io.reactivex.Observable
import net.codysehl.www.reader.Model.Book
import net.codysehl.www.reader.Promise.Deferred
import java.util.*
import kotlin.concurrent.schedule

class SomeKindOfBookSearchRepository(override val kodein: ConfigurableKodein) : BookSearchRepository, KodeinAware {
    val amazonBookSearchService: AmazonBookSearchService = with(kodein).instance()

    override fun search(term: String): Observable<List<Book>> {
        Log.e("Lifecycle", "Searching for term: $term")
        return amazonBookSearchService.search(term)
                .map { response: AmazonBookSearchService.AmazonItemSearchResponse ->
                    response.Items.Items.map {
                        Book(it.ItemAttributes.Title, it.ItemAttributes.Author)
                    }
                }
                .map { // TODO Remove this when the AmazonSearchService stops putting an empty element at the beginning of the list
                    // Not sure why it does that.
                    // Will eventually move the getting-amazon-results code to a server anyways.
                    // Screw you XML
                    it.filter { it != Book("", "") }
                }
    }
}