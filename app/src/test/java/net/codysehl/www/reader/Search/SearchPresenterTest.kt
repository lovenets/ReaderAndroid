package net.codysehl.www.reader.Search

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.singleton
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.reactivex.subjects.PublishSubject
import net.codysehl.www.reader.ReduxLike.Action
import net.codysehl.www.reader.ReduxLike.ApplicationState
import net.codysehl.www.reader.ReduxLike.Store
import org.junit.Test

import org.junit.Before

class SearchPresenterTest {
    lateinit var subject: SearchPresenter
    lateinit var store: Store<ApplicationState>
    lateinit var view: SearchPresenter.View

    lateinit var stateObservable: PublishSubject<ApplicationState>
    lateinit var searchTermChangedObservable: PublishSubject<String>
    lateinit var searchTermSubmittedObservable: PublishSubject<Any>


    @Before
    fun setUp() {
        stateObservable = PublishSubject.create<ApplicationState>()
        searchTermChangedObservable = PublishSubject.create<String>()
        searchTermSubmittedObservable = PublishSubject.create<Any>()

        store = mockk()
        every { store.observable } returns stateObservable
        every { store.dispatch(any()) } returns Unit

        view = mockk()
        every { view.searchTermChanged } returns searchTermChangedObservable
        every { view.searchTermSubmitted } returns searchTermSubmittedObservable

        val kodein = Kodein {
            bind<Store<ApplicationState>>() with singleton { store }
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
        val expectedAction = Action.SearchTermChanged(newSearchTerm)

        searchTermChangedObservable.onNext(newSearchTerm)

        verify { store.dispatch(eq(expectedAction)) }
    }

    @Test
    fun enterPressed_dispatchesAnAction() {
        val expectedAction = Action.SearchSubmitted()

        searchTermSubmittedObservable.onNext(Unit)

        verify { store.dispatch(eq(expectedAction)) }
    }

}