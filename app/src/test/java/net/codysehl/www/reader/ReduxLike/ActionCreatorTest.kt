package net.codysehl.www.reader.ReduxLike

import com.nhaarman.mockito_kotlin.*
import io.reactivex.subjects.PublishSubject
import junit.framework.Assert.assertEquals
import net.codysehl.www.reader.Model.Book
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

        subject = ActionCreator(store, bookSearchRepo)
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