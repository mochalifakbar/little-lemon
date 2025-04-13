package com.embul.littlelemon

import android.util.Log
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class MenuItemNetwork(
    val id: Int,
    val title: String,
    val description: String,
    val price: String,
    val image: String,
    val category: String
)

@Serializable
data class MenuResponse(
    val menu: List<MenuItemNetwork>
)

fun MenuItemNetwork.toMenu() = Menu(
    id = id,
    title = title,
    description = description,
    price = price,
    image = image,
    category = category
)

object Api {
    private const val URL = "https://raw.githubusercontent.com/Meta-Mobile-Developer-PC/Working-With-Data-API/main/menu.json"

    private val httpClient = HttpClient(Android) {
        install(ContentNegotiation) {
            json(
                contentType = ContentType.Application.Json
            )
        }
    }

    suspend fun fetchMenu(): List<MenuItemNetwork> {
        return try {
            val responseString = httpClient.get(URL).body<String>()
            val menuResponse = Json.decodeFromString<MenuResponse>(responseString)
            menuResponse.menu
        } catch (e: Exception) {
            emptyList()
        }
    }
}