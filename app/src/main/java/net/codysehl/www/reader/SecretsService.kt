package net.codysehl.www.reader

import android.content.Context
import java.util.*

class SecretsService(val context: Context?) {
    fun get(key: String): String {
        if(context == null) {
            throw Throwable("No context given to SecretsService, so service can't locate secrets.properties")
        } else {
            val properties = Properties()
            properties.load(context.resources.openRawResource(R.raw.secrets))

            val secret = properties[key]

            when (secret) {
                null -> throw Exception("ERROR: Tried to access secret $key from secrets.properties but it wasn't found.")
                !is String -> throw Exception("ERROR: Tried to access secret $key from secrets.properties but it was not a String")
                else -> return properties[key] as String
            }
        }
    }
}