package net.codysehl.www.reader.Repository

import android.content.Context
import android.util.Log
import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.result.Result
import com.github.salomonbrys.kodein.KodeinAware
import com.github.salomonbrys.kodein.conf.ConfigurableKodein
import com.github.salomonbrys.kodein.instance
import com.github.salomonbrys.kodein.with
import de.codecrafters.apaarb.*
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import net.codysehl.www.reader.SecretsService
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import org.simpleframework.xml.core.Persister

class AmazonBookSearchService(override val kodein: ConfigurableKodein) : KodeinAware {
    val secretsService: SecretsService = instance()

    fun search(term: String): Observable<AmazonItemSearchResponse> {
        val requestUrl = getAmazonProductAdvertisingUrl(term)
        val xmlParser = Persister()
        val requestObservable = PublishSubject.create<AmazonItemSearchResponse>()

        Fuel.get(requestUrl).responseString { request, response, result ->
            Log.e("Lifecycle", "Finished the request: $result")
            when(result) {
                is Result.Success -> {
                    try {
                        val searchResponse: AmazonItemSearchResponse = xmlParser.read(AmazonItemSearchResponse::class.java, result.value)
                        requestObservable.onNext(searchResponse)
                    } catch (e: Exception) {
                        requestObservable.onError(e)
                    }
                }
                is Result.Failure -> requestObservable.onError(Throwable(response.responseMessage))
            }
            requestObservable.onComplete()
        }

        return requestObservable
    }

    private fun getAmazonProductAdvertisingUrl(keyword: String): String {
        val amazonAssociateTag = secretsService.get("AMAZON_PRODUCT_ADVERTISING_ASSOCIATE_TAG")
        val amazonAccessKey = secretsService.get("AMAZON_PRODUCT_ADVERTISING_ACCESS_KEY")
        val amazonSecretKey = secretsService.get("AMAZON_PRODUCT_ADVERTISING_SECRET_KEY")
        val authentication = AmazonWebServiceAuthentication.create(amazonAssociateTag, amazonAccessKey, amazonSecretKey)
        return AmazonProductAdvertisingApiRequestBuilder
                .forItemSearch(keyword)
                .includeInformationAbout(ItemInformation.IMAGES)
                .includeInformationAbout(ItemInformation.ATTRIBUTES)
                .filterByCategroy(ItemCategory.BOOKS)
                .createRequestUrlFor(AmazonWebServiceLocation.COM, authentication)
    }

    @Root(strict = false)
    data class AmazonItemSearchResponse(
            @field:Element var Items: AmazonItems = AmazonItems()
    )

    @Root(strict = false)
    data class AmazonItems(
            @field:Element var Request: AmazonRequest = AmazonRequest(),
            @field:Element var TotalResults: Int = 0,
            @field:Element var TotalPages: Int = 0,
            @field:Element var MoreSearchResultsUrl: String = "",
            @field:ElementList(inline = true) var Items: ArrayList<AmazonItem> = arrayListOf(AmazonItem())
    )

    @Root(strict = false)
    data class AmazonRequest(
            @field:Element var IsValid: Boolean = false
    )

    @Root(strict = false, name = "Item")
    data class AmazonItem(
            @field:Element var ASIN: String = "",
            @field:Element var SmallImage: AmazonSmallImage = AmazonSmallImage(),
            @field:Element var ItemAttributes: AmazonItemAttributes = AmazonItemAttributes()
    )

    @Root(strict = false)
    data class AmazonSmallImage(
            @field:Element var URL: String = ""
    )

    @Root(strict = false)
    data class AmazonItemAttributes(
            @field:Element(required = false) var Author: String = "",
            @field:Element(required = false) var Title: String = ""
    )
}