package net.codysehl.www.reader.Repository

import android.util.Log
import com.github.salomonbrys.kodein.KodeinAware
import com.github.salomonbrys.kodein.conf.ConfigurableKodein
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.with
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import net.codysehl.www.reader.Model.Book
import net.codysehl.www.reader.Promise.Deferred
import java.util.*
import kotlin.concurrent.schedule

class SomeKindOfBookSearchRepository(override val kodein: ConfigurableKodein) : BookSearchRepository, KodeinAware {
    val googleBookSearchService: GoogleBooksService = instance()

    override fun search(term: String): Observable<List<Book>> {
        Log.e("Lifecycle", "Searching for term: $term")

        return googleBookSearchService.search(term)
                .map { response: GoogleBooksResponse ->
                    response.items.map {
                        Book(it.volumeInfo.title, it.volumeInfo.authors.firstOrNull() ?: "")
                    }
                }
                .subscribeOn(Schedulers.io())
    }
}