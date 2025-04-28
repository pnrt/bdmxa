package com.pankajkumarrout.bdmxa.network

import com.pankajkumarrout.bdmxa.model.ForAuthentication
import com.pankajkumarrout.bdmxa.model.User
import com.pankajkumarrout.bdmxa.security.Api
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json


class LoginApiService {
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

    suspend fun logUser(forAuthentication: ForAuthentication): User {
        return client.get("${Api.url}${Api.apiKey}/user/verify") {
            contentType(ContentType.Application.Json)
            setBody(forAuthentication)
        }.body<User>()
    }
}