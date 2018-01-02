package net.codysehl.www.reader

import java.io.File
import java.io.FileInputStream
import java.util.*

object SecretsService {
    fun get(key: String): String {
        val properties = Properties()
        properties.load(FileInputStream(File("secrets.properties")))

        val secret = properties[key]

        when (secret) {
            null -> throw Exception("ERROR: Tried to access secret $key from secrets.properties but it wasn't found.")
            !is String -> throw Exception("ERROR: Tried to access secret $key from secrets.properties but it was not a String")
            else -> return properties[key] as String
        }
    }
}