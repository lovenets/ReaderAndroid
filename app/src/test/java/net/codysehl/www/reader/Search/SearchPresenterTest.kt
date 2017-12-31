package net.codysehl.www.reader.Search

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.singleton
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import net.codysehl.www.reader.ReduxLike.ActionCreator
import net.codysehl.www.reader.ReduxLike.ApplicationState
import org.junit.Test

import org.junit.Before

class SearchPresenterTest {
    lateinit var subject: SearchPresenter
    lateinit var view: SearchPresenter.View
    lateinit var actionCreator: ActionCreator

    lateinit var stateObservable: PublishSubject<ApplicationState>
    lateinit var searchTermChangedObservable: PublishSubject<String>
    lateinit var searchTermSubmittedObservable: PublishSubject<Any>

    @Before
    fun setUp() {
        stateObservable = PublishSubject.create<ApplicationState>()
        searchTermChangedObservable = PublishSubject.create<String>()
        searchTermSubmittedObservable = PublishSubject.create<Any>()

        view = mockk()
        every { view.searchTermChanged } returns searchTermChangedObservable
        every { view.searchTermSubmitted } returns searchTermSubmittedObservable

        actionCreator = mockk()

        val kodein = Kodein {
            bind<Observable<ApplicationState>>() with singleton { stateObservable }
            bind<ActionCreator>() with singleton { actionCreator }
        }

        subject = SearchPresenter(kodein)
        subject.onViewReady(view)
    }

    @Test
    fun viewReady_setsUpRendering() {
        val textEntered = "some new text"
        stateObservable.onNext(ApplicationState(textEntered))

        verify { view.render(eq(SearchPresenter.Props(textEntered))) }
    }

    @Test
    fun searchTermChanged_dispatchesAnAction() {
        val newSearchTerm = "search term"

        searchTermChangedObservable.onNext(newSearchTerm)

        verify { actionCreator.searchTermChanged(newSearchTerm) }
    }

    @Test
    fun enterPressed_dispatchesAnAction() {
        searchTermSubmittedObservable.onNext(Unit)

        verify { actionCreator.searchSubmitted() }
    }

}