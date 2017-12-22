package net.codysehl.www.reader

import com.github.salomonbrys.kodein.Kodein
import com.github.salomonbrys.kodein.bind
import com.github.salomonbrys.kodein.singleton
import net.codysehl.www.reader.ReduxLike.ApplicationState
import net.codysehl.www.reader.ReduxLike.Reducer
import net.codysehl.www.reader.ReduxLike.Store

object KodeinModule {
    val module = Kodein {
        bind<Store<ApplicationState>>() with singleton { Store(Reducer::reduce, ApplicationState()) }
    }
}