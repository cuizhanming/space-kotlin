package com.kotlinkoog.journey.models

import kotlinx.serialization.Serializable

@Serializable
data class JourneyPlanRequest(
    val destination: String,
    val startDate: String, // ISO date format (YYYY-MM-DD)
    val endDate: String,   // ISO date format (YYYY-MM-DD)
    val preferences: JourneyPreferences? = null
)

@Serializable
data class JourneyPreferences(
    val budget: String? = null, // "low", "medium", "high"
    val travelStyle: String? = null, // "adventure", "relaxation", "cultural", "family"
    val accommodation: String? = null, // "hotel", "hostel", "airbnb", "any"
    val transportation: String? = null, // "flight", "train", "car", "any"
    val interests: List<String> = emptyList() // e.g., ["museums", "beaches", "hiking", "nightlife"]
)

@Serializable
data class JourneyPlanResponse(
    val destination: String,
    val dateRange: String,
    val itinerary: List<DayPlan>,
    val travelTips: List<String>,
    val estimatedBudget: String? = null,
    val weatherInfo: String? = null
)

@Serializable
data class DayPlan(
    val day: Int,
    val date: String,
    val activities: List<Activity>,
    val meals: List<Meal> = emptyList(),
    val accommodation: String? = null,
    val notes: String? = null
)

@Serializable
data class Activity(
    val time: String, // e.g., "09:00"
    val name: String,
    val description: String,
    val location: String,
    val duration: String, // e.g., "2 hours"
    val estimatedCost: String? = null,
    val category: String // e.g., "sightseeing", "adventure", "culture"
)

@Serializable
data class Meal(
    val time: String, // "breakfast", "lunch", "dinner"
    val restaurant: String,
    val cuisine: String,
    val location: String,
    val estimatedCost: String? = null
)

@Serializable
data class ErrorResponse(
    val error: String,
    val message: String
)