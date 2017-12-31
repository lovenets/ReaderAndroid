package net.codysehl.www.reader

import com.github.salomonbrys.kodein.*
import com.github.salomonbrys.kodein.conf.ConfigurableKodein
import io.reactivex.Observable
import net.codysehl.www.reader.ReduxLike.ActionCreator
import net.codysehl.www.reader.ReduxLike.ApplicationState
import net.codysehl.www.reader.ReduxLike.Reducer
import net.codysehl.www.reader.ReduxLike.Store
import net.codysehl.www.reader.Search.SearchPresenter

fun KodeinModule(): Kodein.Module {
    return Kodein.Module {
        val store = Store(Reducer::reduce, ApplicationState())
        bind<Store<ApplicationState>>() with singleton { store }
        bind<Observable<ApplicationState>>() with singleton { store.observable }

        bind<ActionCreator>() with singleton { ActionCreator() }

        bind<SearchPresenter>() with factory { kodein: ConfigurableKodein -> SearchPresenter(kodein) }
    }
}