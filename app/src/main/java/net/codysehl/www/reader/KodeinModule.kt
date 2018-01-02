package net.codysehl.www.reader

import com.github.salomonbrys.kodein.*
import com.github.salomonbrys.kodein.conf.ConfigurableKodein
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import net.codysehl.www.reader.ReduxLike.ActionCreator
import net.codysehl.www.reader.ReduxLike.ApplicationState
import net.codysehl.www.reader.ReduxLike.Reducer
import net.codysehl.www.reader.ReduxLike.Store
import net.codysehl.www.reader.Repository.BookSearchRepository
import net.codysehl.www.reader.Repository.SomeKindOfBookSearchRepository
import net.codysehl.www.reader.Search.SearchPresenter

fun KodeinModule(): Kodein.Module {
    return Kodein.Module {
        val store = Store(Reducer::reduce, ApplicationState())
        bind<Store<ApplicationState>>() with singleton { store }
        bind<Observable<ApplicationState>>() with singleton { store.observable }

        bind<Scheduler>(KodeinTag.MAIN_THREAD) with provider { AndroidSchedulers.mainThread() }

        bind<ActionCreator>() with factory { kodein: ConfigurableKodein -> ActionCreator(kodein) }

        bind<SearchPresenter>() with factory { kodein: ConfigurableKodein -> SearchPresenter(kodein) }

        bind<BookSearchRepository>() with provider { SomeKindOfBookSearchRepository() }
    }
}

enum class KodeinTag {
    MAIN_THREAD
}