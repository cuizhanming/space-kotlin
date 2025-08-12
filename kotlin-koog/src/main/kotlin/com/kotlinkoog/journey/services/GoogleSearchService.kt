package com.kotlinkoog.journey.services

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class SearchResult(
    val title: String,
    val link: String,
    val snippet: String
)

@Serializable
data class GoogleSearchResponse(
    val items: List<GoogleSearchItem>? = null
)

@Serializable
data class GoogleSearchItem(
    val title: String,
    val link: String,
    val snippet: String
)

class GoogleSearchService(private val apiKey: String, private val searchEngineId: String) {
    
    private val httpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }
    
    suspend fun search(query: String, maxResults: Int = 10): List<SearchResult> {
        return try {
            val response: GoogleSearchResponse = httpClient.get("https://www.googleapis.com/customsearch/v1") {
                parameter("key", apiKey)
                parameter("cx", searchEngineId)
                parameter("q", query)
                parameter("num", maxResults)
            }.body()
            
            response.items?.map { item ->
                SearchResult(
                    title = item.title,
                    link = item.link,
                    snippet = item.snippet
                )
            } ?: emptyList()
        } catch (e: Exception) {
            println("Search error: ${e.message}")
            emptyList()
        }
    }
    
    suspend fun searchTravelInfo(destination: String): List<SearchResult> {
        val queries = listOf(
            "best things to do in $destination 2024",
            "travel guide $destination attractions",
            "$destination restaurants local food",
            "$destination hotels accommodation",
            "$destination weather climate best time visit"
        )
        
        val allResults = mutableListOf<SearchResult>()
        
        for (query in queries) {
            val results = search(query, 3)
            allResults.addAll(results)
        }
        
        return allResults.take(15) // Limit total results
    }
    
    suspend fun searchActivityInfo(destination: String, activity: String): List<SearchResult> {
        val query = "$activity in $destination hours cost location"
        return search(query, 5)
    }
    
    suspend fun searchRestaurantInfo(destination: String, cuisine: String? = null): List<SearchResult> {
        val query = if (cuisine != null) {
            "$cuisine restaurants in $destination reviews ratings"
        } else {
            "best restaurants in $destination local food"
        }
        return search(query, 5)
    }
    
    fun close() {
        httpClient.close()
    }
}