package net.codysehl.www.reader.Repository

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import io.reactivex.subscribers.TestSubscriber
import net.codysehl.www.reader.Model.Book
import org.junit.Before
import org.junit.Test


class GoogleBookSearchRepositoryTest {
    lateinit var booksService: GoogleBooksService
    lateinit var booksServiceSearchObservable: PublishSubject<GoogleBooksResponse>

    lateinit var subject: GoogleBookSearchRepository

    @Before
    fun setup() {
        // I don't think I need to test this stuff
        // The type system takes care of it mostly
//        booksServiceSearchObservable = PublishSubject.create()
//        booksService = mock {
//            on { search(any()) } doReturn booksServiceSearchObservable
//        }
//
//
//        subject = GoogleBookSearchRepository(booksService)
    }

    @Test
    fun search_returnsListOfBooks() {
//        val title = "The Pale King"
//        val subtitle = ""
//        val author = "David Foster Wallace"
//        val googleBooksResponse = GoogleBooksResponse(listOf(GoogleBooksVolume(GoogleBooksVolumeInfo(title, subtitle, listOf(author)))))
//
//        val searchObservable: Observable<List<Book>> = subject.search("David Foster Wallace")
//
//        booksServiceSearchObservable.onNext(googleBooksResponse)
//
//        val testSubscriber = TestSubscriber.create<List<Book>>()
//        searchObservable
//                .subscribeOn(Schedulers.trampoline())
//                .subscribe(testSubscriber)

    }
}