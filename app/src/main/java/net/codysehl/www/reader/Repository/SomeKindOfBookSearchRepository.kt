package net.codysehl.www.reader.Repository

import net.codysehl.www.reader.Model.Book
import net.codysehl.www.reader.Promise.Deferred
import java.util.*
import kotlin.concurrent.schedule

class SomeKindOfBookSearchRepository: BookSearchRepository {
    override fun search(term: String): Deferred.Promise<List<Book>, Throwable> {
        val deferred = Deferred<List<Book>, Throwable>()
        Timer().schedule(1000) {
            deferred.resolve(listOf(Book("")))
        }
        return deferred.promise
    }
}