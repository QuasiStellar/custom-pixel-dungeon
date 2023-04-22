package com.qsr.customspd.utils

import com.watabou.utils.DeviceCompat
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.ANDROID
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object Network {

    inline fun <T> access(block: HttpClient.() -> T) = httpClient.use(block)

    val httpClient get() = HttpClient(OkHttp) {
        if (DeviceCompat.isDebug()) {
            install(Logging) {
                logger = if (DeviceCompat.isAndroid()) Logger.ANDROID else Logger.DEFAULT
            }
        }
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }
}
