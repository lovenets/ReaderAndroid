package net.codysehl.www.reader.Repository

import io.reactivex.Observable
import net.codysehl.www.reader.Model.Book

interface BookSearchRepository {
    fun search(term: String): Observable<List<Book>>
}