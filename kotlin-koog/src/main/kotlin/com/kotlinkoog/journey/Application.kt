package com.kotlinkoog.journey

import com.kotlinkoog.journey.api.journeyRoutes
import com.kotlinkoog.journey.config.ConfigLoader
import com.kotlinkoog.journey.services.JourneyPlannerService
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

fun main() {
    // Load configuration
    val config = try {
        ConfigLoader.load()
    } catch (e: Exception) {
        println("Failed to load configuration: ${e.message}")
        println("Please ensure all required environment variables are set.")
        println("Check .env.example for required variables.")
        return
    }
    
    // Create journey planner service
    val journeyPlannerService = JourneyPlannerService(
        geminiApiKey = config.gemini.apiKey,
        googleSearchApiKey = config.googleSearch.apiKey,
        searchEngineId = config.googleSearch.searchEngineId
    )
    
    // Start server
    embeddedServer(
        Netty,
        port = config.server.port,
        host = config.server.host,
        module = { module(journeyPlannerService) }
    ).start(wait = true)
}

fun Application.module(journeyPlannerService: JourneyPlannerService) {
    // Configure JSON serialization
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            isLenient = true
            ignoreUnknownKeys = true
        })
    }
    
    // Configure routing
    routing {
        // Root endpoint
        get("/") {
            call.respond(
                mapOf(
                    "service" to "AI Journey Planner",
                    "description" to "Create personalized travel itineraries using Koog AI framework with Gemini LLM and Google Search",
                    "version" to "1.0.0",
                    "endpoints" to mapOf(
                        "GET /" to "Service information",
                        "POST /api/v1/journey/plan" to "Create a journey plan",
                        "GET /api/v1/journey/health" to "Health check"
                    ),
                    "framework" to "Koog AI",
                    "powered_by" to listOf("Gemini AI", "Google Custom Search", "Ktor")
                )
            )
        }
        
        // Journey planning routes
        journeyRoutes(journeyPlannerService)
    }
    
    // Shutdown hook to close services
    environment.monitor.subscribe(ApplicationStopping) {
        journeyPlannerService.close()
    }
}