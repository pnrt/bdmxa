package com.pankajkumarrout.bdmxa.network

import com.pankajkumarrout.bdmxa.model.OrderInfo
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import com.pankajkumarrout.bdmxa.security.Api
import com.pankajkumarrout.bdmxa.ui.login.LogUser

class OrderApiService {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                coerceInputValues = true
                prettyPrint = true
                isLenient = true
            })
        }
    }

    suspend fun orderInfoList(id: Long): List<OrderInfo> {
        return client.get("${Api.url}${Api.apiKey}/order/${if (LogUser.presentUser?.role == "admin") "list" else "active"}/${id}").body<List<OrderInfo>>()
    }
}