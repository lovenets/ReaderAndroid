package net.codysehl.www.reader.Repository

import net.codysehl.www.reader.Model.Book
import net.codysehl.www.reader.Promise.Deferred

interface BookSearchRepository {
    fun search(term: String): Deferred.Promise<List<Book>, Throwable>
}