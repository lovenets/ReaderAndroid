package net.codysehl.www.reader.ReduxLike

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.conf.ConfigurableKodein
import com.github.salomonbrys.kodein.conf.global
import com.github.salomonbrys.kodein.factory
import com.github.salomonbrys.kodein.singleton
import com.nhaarman.mockito_kotlin.*
import io.reactivex.subjects.PublishSubject
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import net.codysehl.www.reader.Model.Book
import net.codysehl.www.reader.Promise.Deferred
import net.codysehl.www.reader.Repository.BookSearchRepository
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito

class ActionCreatorTest {

    lateinit var store: Store<ApplicationState>
    lateinit var bookSearchRepo: BookSearchRepository

    lateinit var bookSearchObservable: PublishSubject<List<Book>>

    lateinit var subject: ActionCreator

    @Before
    fun setUp() {
        fun reducer(state: ApplicationState, action: Action): ApplicationState = state
        store = Mockito.spy(Store(::reducer, ApplicationState("David Foster Wallace")))

        bookSearchObservable = PublishSubject.create()
        bookSearchRepo = mock {
            on { search(any()) } doReturn bookSearchObservable
        }

        val kodein = ConfigurableKodein()
        kodein.addConfig {
            bind<Store<ApplicationState>>() with singleton { store }
            bind<BookSearchRepository>() with factory<ConfigurableKodein, BookSearchRepository> { _ -> bookSearchRepo }
        }

        subject = ActionCreator(kodein)
    }

    @Test
    fun searchSubmitted_dispatchesAnAction() {
        subject.searchSubmitted()

        verify(store).dispatch(any<Action.SearchSubmitted>())
    }

    @Test
    fun searchSubmitted_success_dispatchesAnAction() {
        val expectedBooks = listOf(Book("Consider the Lobster", "David Foster Wallace"))

        subject.searchSubmitted()
        Mockito.reset(store)

        bookSearchObservable.onNext(expectedBooks)

        verify(store).dispatch(any<Action.SearchCompletedWithSuccess>())
        verify(store).dispatch(check {
            assertEquals((it as Action.SearchCompletedWithSuccess).books, expectedBooks)
        })
    }

    @Test
    fun searchSubmitted_failure_dispatchesAnAction() {
        subject.searchSubmitted()
        Mockito.reset(store)

        bookSearchObservable.onError(Throwable())

        verify(store).dispatch(any<Action.SearchCompletedWithFailure>())
    }

}