package net.codysehl.www.reader.Search

import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import net.codysehl.www.reader.ReduxLike.ActionCreator
import net.codysehl.www.reader.ReduxLike.ApplicationState
import net.codysehl.www.reader.ReduxLike.Store
import net.codysehl.www.reader.Repository.GoogleBooksResponse
import org.junit.Before
import org.junit.Test

class SearchIntegrationTest {

    lateinit var searchPresenter: SearchPresenter
    lateinit var view: SearchPresenter.View
    lateinit var searchTermChangedObservable: PublishSubject<String>
    lateinit var searchTermSubmittedObservable: PublishSubject<Any>
    lateinit var amazonSearchResultObservable: PublishSubject<GoogleBooksResponse>

    @Before
    fun setUp() {
        searchTermChangedObservable = PublishSubject.create()
        searchTermSubmittedObservable = PublishSubject.create()
        amazonSearchResultObservable = PublishSubject.create()

        view = mock {
            on { searchTermChanged } doReturn searchTermChangedObservable
            on { searchTermSubmitted } doReturn searchTermSubmittedObservable
        }

        searchPresenter = SearchPresenter(Store.singleton.observable, ActionCreator.create(), Schedulers.trampoline())
    }

    @Test
    fun searchTermChanged() {
        val searchTerm = "David Foster Wallace"
        val expectedRenderedProps = SearchPresenter.Props(
                searchText = searchTerm,
                showLoadingSpinner = false,
                disableSearchBar = false,
                disableSearchSubmitButton = false,
                books = listOf()
            )
        searchPresenter.onViewReady(view)

        searchTermChangedObservable.onNext(searchTerm)

        verify(view).render(expectedRenderedProps)
    }
}