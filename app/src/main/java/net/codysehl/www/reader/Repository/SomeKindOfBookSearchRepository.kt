package net.codysehl.www.reader.Repository

import net.codysehl.www.reader.Model.Book
import net.codysehl.www.reader.Promise.Deferred

class SomeKindOfBookSearchRepository: BookSearchRepository {
    override fun search(term: String): Deferred.Promise<List<Book>, Throwable> {
        val deferred = Deferred<List<Book>, Throwable>()
        deferred.resolve(listOf(Book("")))
        return deferred.promise
    }
}