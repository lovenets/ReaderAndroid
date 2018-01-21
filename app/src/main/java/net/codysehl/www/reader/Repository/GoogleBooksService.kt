package net.codysehl.www.reader.Repository

import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query


// https://www.googleapis.com/books/v1/volumes?q={search terms}
interface GoogleBooksService {
    @GET("volumes")
    fun search(@Query("q") term: String): Observable<GoogleBooksResponse>
}

data class GoogleBooksResponse(
    val items: List<GoogleBooksVolume>
)

data class GoogleBooksVolume(
    val volumeInfo: GoogleBooksVolumeInfo
)

data class GoogleBooksVolumeInfo(
        val title: String = "",
        val subtitle: String = "",
        val authors: List<String> = listOf()
)