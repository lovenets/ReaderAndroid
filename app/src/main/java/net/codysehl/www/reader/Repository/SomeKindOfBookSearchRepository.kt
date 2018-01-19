package net.codysehl.www.reader.Repository

import android.util.Log
import io.reactivex.Observable
import io.reactivex.schedulers.Schedulers
import net.codysehl.www.reader.Model.Book
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class SomeKindOfBookSearchRepository(private val googleBookSearchService: GoogleBooksService) : BookSearchRepository {
    override fun search(term: String): Observable<List<Book>> {
        Log.e("Lifecycle", "Searching for term: $term")

        return googleBookSearchService.search(term)
                .map { response: GoogleBooksResponse ->
                    response.items.map {
                        Book(it.volumeInfo.title, it.volumeInfo.authors.firstOrNull() ?: "")
                    }
                }
                .subscribeOn(Schedulers.io())
    }

    companion object {
        fun create(): BookSearchRepository {
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
            val googleBooksService: GoogleBooksService = retrofit.create(GoogleBooksService::class.java)

            return SomeKindOfBookSearchRepository(googleBooksService)
        }
    }
}