package net.codysehl.www.reader

import android.content.Context
import android.util.Log
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
import net.codysehl.www.reader.Repository.GoogleBooksService
import net.codysehl.www.reader.Repository.SomeKindOfBookSearchRepository
import net.codysehl.www.reader.Search.SearchPresenter
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

fun KodeinModule(): Kodein.Module {
    return Kodein.Module {
        val store = Store(Reducer::reduce, ApplicationState())
        bind<Store<ApplicationState>>() with singleton { store }
        bind<Observable<ApplicationState>>() with singleton { store.observable }

        bind<Scheduler>(KodeinTag.MAIN_THREAD) with provider { AndroidSchedulers.mainThread() }

        bind<ActionCreator>() with factory { kodein: ConfigurableKodein -> ActionCreator(kodein) }

        bind<SearchPresenter>() with factory { kodein: ConfigurableKodein -> SearchPresenter(kodein) }

        bind<BookSearchRepository>() with factory { kodein: ConfigurableKodein -> SomeKindOfBookSearchRepository(kodein) }

        bind<Logger>() with singleton { Logger() }

        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClientBuilder = OkHttpClient.Builder()
        httpClientBuilder.interceptors().add(logging)
        val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/books/v1/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClientBuilder.build())
                .build()
        bind<GoogleBooksService>() with singleton { retrofit.create(GoogleBooksService::class.java) }

    }
}

enum class KodeinTag {
    MAIN_THREAD
}