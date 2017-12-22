package net.codysehl.www.reader.Search

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.conf.global
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
    lateinit var stateObservable: PublishSubject<ApplicationState>

    @Before
    fun setUp() {
        stateObservable = PublishSubject.create<ApplicationState>()
        store = mockk()
        every { store.observable } returns stateObservable
        every { store.dispatch(any()) } returns Unit

        Kodein.global.addConfig {
            bind<Store<ApplicationState>>() with singleton { store }
        }

        subject = SearchPresenter()
    }

    @Test
    fun textEntered_dispatchesATextEnteredAction() {
        val textToEnter = "search term"

        subject.textEntered(textToEnter)

        verify { store.dispatch(ofType(Action.SearchTextEntered::class)) }
        verify { store.dispatch(match<Action.SearchTextEntered> { it.text == textToEnter }) }

    }

}