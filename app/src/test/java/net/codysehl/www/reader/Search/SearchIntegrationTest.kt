package net.codysehl.www.reader.Search

import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.conf.ConfigurableKodein
import com.github.salomonbrys.kodein.singleton
import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.subjects.PublishSubject
import net.codysehl.www.reader.KodeinModule
import net.codysehl.www.reader.Repository.GoogleBooksResponse
import net.codysehl.www.reader.Repository.GoogleBooksService
import net.codysehl.www.reader.SchedulerModule
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
        searchTermChangedObservable = PublishSubject.create<String>()
        searchTermSubmittedObservable = PublishSubject.create<Any>()
        amazonSearchResultObservable = PublishSubject.create<GoogleBooksResponse>()

        view = mock {
            on { searchTermChanged } doReturn searchTermChangedObservable
            on { searchTermSubmitted } doReturn searchTermSubmittedObservable
        }

        val bookSearchService: GoogleBooksService = mock {
            on { search(any()) } doReturn amazonSearchResultObservable
        }

        val kodein = ConfigurableKodein()
        kodein.addImport(KodeinModule(), allowOverride = true)
        kodein.addImport(SchedulerModule(overrides = true), allowOverride = true)
        kodein.addConfig {
            bind<GoogleBooksService>(overrides = true) with singleton { bookSearchService }
        }
        searchPresenter = SearchPresenter(kodein)
    }

    @Test
    fun searchTermChanged() {
        val searchTerm = "David Foster Wallace"
        val searchResults = GoogleBooksResponse(listOf())
        val expectedRenderedProps = SearchPresenter.Props(
                searchText = searchTerm,
                showLoadingSpinner = false,
                disableSearchBar = false,
                disableSearchSubmitButton = false,
                books = listOf()
            )
        searchPresenter.onViewReady(view)

        searchTermChangedObservable.onNext(searchTerm)
        amazonSearchResultObservable.onNext(searchResults)

        verify(view).render(expectedRenderedProps)
    }
}