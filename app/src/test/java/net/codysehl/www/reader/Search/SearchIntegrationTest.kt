package net.codysehl.www.reader.Search

import com.github.salomonbrys.kodein.conf.ConfigurableKodein
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.subjects.PublishSubject
import net.codysehl.www.reader.KodeinModule
import net.codysehl.www.reader.SchedulerModule
import org.junit.Before
import org.junit.Test

class SearchIntegrationTest {

    lateinit var searchPresenter: SearchPresenter
    lateinit var view: SearchPresenter.View
    lateinit var searchTermChangedObservable: PublishSubject<String>
    lateinit var searchTermSubmittedObservable: PublishSubject<Any>

    @Before
    fun setUp() {
        searchTermChangedObservable = PublishSubject.create<String>()
        searchTermSubmittedObservable = PublishSubject.create<Any>()


        view = mock {
            on { searchTermChanged } doReturn searchTermChangedObservable
            on { searchTermSubmitted } doReturn searchTermSubmittedObservable
        }

        val kodein = ConfigurableKodein()
        kodein.addImport(KodeinModule(), allowOverride = true)
        kodein.addImport(SchedulerModule(overrides = true), allowOverride = true)
        searchPresenter = SearchPresenter(kodein)
    }

    @Test
    fun searchTermChanged() {
        val searchTerm = "David Foster Wallace"
        val expectedRenderedProps = SearchPresenter.Props(
                searchText = searchTerm,
                showLoadingSpinner = false,
                disableSearchBar = false,
                disableSearchSubmitButton = false
            )
        searchPresenter.onViewReady(view)

        searchTermChangedObservable.onNext(searchTerm)

        verify(view).render(expectedRenderedProps)
    }
}