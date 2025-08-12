package com.kotlinkoog.journey.api

import com.kotlinkoog.journey.models.ErrorResponse
import com.kotlinkoog.journey.models.JourneyPlanRequest
import com.kotlinkoog.journey.services.JourneyPlannerService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withTimeout
import java.time.LocalDate
import java.time.format.DateTimeParseException

fun Route.journeyRoutes(journeyPlannerService: JourneyPlannerService) {
    
    route("/api/v1/journey") {
        
        post("/plan") {
            try {
                val request = call.receive<JourneyPlanRequest>()
                
                // Validate request
                val validationError = validateJourneyRequest(request)
                if (validationError != null) {
                    call.respond(
                        HttpStatusCode.BadRequest,
                        ErrorResponse("VALIDATION_ERROR", validationError)
                    )
                    return@post
                }
                
                // Create journey plan with timeout
                val journeyPlan = withTimeout(60_000) { // 60 seconds timeout
                    journeyPlannerService.createEnhancedJourneyPlan(request)
                }
                
                call.respond(HttpStatusCode.OK, journeyPlan)
                
            } catch (e: TimeoutCancellationException) {
                call.respond(
                    HttpStatusCode.RequestTimeout,
                    ErrorResponse("TIMEOUT", "Request timed out. Please try again.")
                )
            } catch (e: Exception) {
                call.application.log.error("Error creating journey plan", e)
                call.respond(
                    HttpStatusCode.InternalServerError,
                    ErrorResponse("INTERNAL_ERROR", "An error occurred while creating the journey plan")
                )
            }
        }
        
        get("/health") {
            call.respond(
                HttpStatusCode.OK,
                mapOf(
                    "status" to "healthy",
                    "service" to "journey-planner",
                    "version" to "1.0.0"
                )
            )
        }
        
        get("/") {
            call.respond(
                HttpStatusCode.OK,
                mapOf(
                    "service" to "AI Journey Planner",
                    "description" to "Create personalized travel itineraries using AI",
                    "endpoints" to mapOf(
                        "POST /api/v1/journey/plan" to "Create a journey plan",
                        "GET /api/v1/journey/health" to "Health check"
                    ),
                    "version" to "1.0.0"
                )
            )
        }
    }
}

private fun validateJourneyRequest(request: JourneyPlanRequest): String? {
    // Validate destination
    if (request.destination.isBlank()) {
        return "Destination cannot be empty"
    }
    
    // Validate dates
    try {
        val startDate = LocalDate.parse(request.startDate)
        val endDate = LocalDate.parse(request.endDate)
        
        if (endDate.isBefore(startDate)) {
            return "End date must be after start date"
        }
        
        if (startDate.isBefore(LocalDate.now())) {
            return "Start date cannot be in the past"
        }
        
        val maxTripDuration = 30L // 30 days maximum
        if (java.time.temporal.ChronoUnit.DAYS.between(startDate, endDate) > maxTripDuration) {
            return "Trip duration cannot exceed $maxTripDuration days"
        }
        
    } catch (e: DateTimeParseException) {
        return "Invalid date format. Use YYYY-MM-DD format"
    }
    
    // Validate preferences
    request.preferences?.let { prefs ->
        prefs.budget?.let { budget ->
            if (budget.lowercase() !in listOf("low", "medium", "high")) {
                return "Budget must be 'low', 'medium', or 'high'"
            }
        }
        
        prefs.travelStyle?.let { style ->
            if (style.lowercase() !in listOf("adventure", "relaxation", "cultural", "family", "business")) {
                return "Invalid travel style"
            }
        }
        
        prefs.accommodation?.let { accommodation ->
            if (accommodation.lowercase() !in listOf("hotel", "hostel", "airbnb", "resort", "any")) {
                return "Invalid accommodation type"
            }
        }
        
        prefs.transportation?.let { transport ->
            if (transport.lowercase() !in listOf("flight", "train", "car", "bus", "any")) {
                return "Invalid transportation type"
            }
        }
    }
    
    return null
}