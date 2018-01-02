package net.codysehl.www.reader.Repository

import de.codecrafters.apaarb.*
import net.codysehl.www.reader.SecretsService
import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Root
import org.simpleframework.xml.core.Persister
import java.net.URL

class AmazonBookSearchService {
    fun search(keyword: String): AmazonItemSearchResponse {

        val requestUrl = getAmazonProductAdvertisingUrl(keyword)
        val connection = requestUrl.openConnection()

        val serializer = Persister()
        val parsedResponse: AmazonItemSearchResponse = serializer.read(AmazonItemSearchResponse::class.java, connection.getInputStream())

        return parsedResponse
    }

    private fun getAmazonProductAdvertisingUrl(keyword: String): URL {
        val amazonAssociateTag = SecretsService.get("AMAZON_PRODUCT_ADVERTISING_ASSOCIATE_TAG")
        val amazonAccessKey = SecretsService.get("AMAZON_PRODUCT_ADVERTISING_ACCESS_KEY")
        val amazonSecretKey = SecretsService.get("AMAZON_PRODUCT_ADVERTISING_SECRET_KEY")
        val authentication = AmazonWebServiceAuthentication.create(amazonAssociateTag, amazonAccessKey, amazonSecretKey)
        return URL(AmazonProductAdvertisingApiRequestBuilder
                .forItemSearch(keyword)
                .includeInformationAbout(ItemInformation.IMAGES)
                .includeInformationAbout(ItemInformation.ATTRIBUTES)
                .filterByCategroy(ItemCategory.BOOKS)
                .createRequestUrlFor(AmazonWebServiceLocation.COM, authentication))
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
            @field:Element var Author: String = "",
            @field:Element var Title: String = ""
    )
}