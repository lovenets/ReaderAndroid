package net.codysehl.www.reader

import com.github.salomonbrys.kodein.*
import com.github.salomonbrys.kodein.conf.ConfigurableKodein
import net.codysehl.www.reader.ReduxLike.ApplicationState
import net.codysehl.www.reader.ReduxLike.Reducer
import net.codysehl.www.reader.ReduxLike.Store
import net.codysehl.www.reader.Search.SearchPresenter

fun KodeinModule(): Kodein.Module {
    return Kodein.Module {
        bind<Store<ApplicationState>>() with singleton { Store(Reducer::reduce, ApplicationState()) }

        bind<SearchPresenter>() with factory { kodein: ConfigurableKodein -> SearchPresenter(kodein) }
    }
}