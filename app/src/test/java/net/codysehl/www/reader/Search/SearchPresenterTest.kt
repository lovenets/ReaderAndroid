package net.codysehl.www.reader.Search

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.doReturn
import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import net.codysehl.www.reader.ReduxLike.ActionCreator
import net.codysehl.www.reader.ReduxLike.ApplicationState
import org.junit.Before
import org.junit.Test

class SearchPresenterTest {
    lateinit var subject: SearchPresenter
    lateinit var view: SearchPresenter.View
    lateinit var actionCreator: ActionCreator

    lateinit var applicationStateObservable: PublishSubject<ApplicationState>
    lateinit var searchTermChangedObservable: PublishSubject<String>
    lateinit var searchTermSubmittedObservable: PublishSubject<Any>

    @Before
    fun setUp() {
        applicationStateObservable = PublishSubject.create<ApplicationState>()
        searchTermChangedObservable = PublishSubject.create<String>()
        searchTermSubmittedObservable = PublishSubject.create<Any>()

        view = mock {
            on { searchTermChanged } doReturn searchTermChangedObservable
            on { searchTermSubmitted } doReturn searchTermSubmittedObservable
        }

        actionCreator = mock()

        subject = SearchPresenter(applicationStateObservable, actionCreator, Schedulers.trampoline())
        subject.onViewReady(view)
    }

    @Test
    fun viewReady_setsUpRendering() {
        val textEntered = "some new text"
        applicationStateObservable.onNext(ApplicationState(textEntered))

        verify(view).render(any())
    }

    @Test
    fun searchTermChanged_dispatchesAnAction() {
        val newSearchTerm = "search term"

        searchTermChangedObservable.onNext(newSearchTerm)

        verify(actionCreator).searchTermChanged(newSearchTerm)
    }

    @Test
    fun enterPressed_dispatchesAnAction() {
        searchTermSubmittedObservable.onNext(Unit)

        verify(actionCreator).searchSubmitted()
    }

}