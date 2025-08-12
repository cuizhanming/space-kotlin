package com.kotlinkoog.journey.services

import com.kotlinkoog.journey.models.*
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class JourneyPlannerService(
    private val geminiApiKey: String,
    private val googleSearchApiKey: String,
    private val searchEngineId: String
) {
    private val geminiService = GeminiService(geminiApiKey)
    private val searchService = GoogleSearchService(googleSearchApiKey, searchEngineId)
    
    // TODO: Replace with actual Koog AI agent once API is confirmed
    private val systemPrompt = """
        You are an expert AI travel agent that creates comprehensive, personalized journey plans.
        
        Your role is to:
        1. Analyze travel requests and preferences
        2. Use real-time search data to find current information about destinations
        3. Create detailed day-by-day itineraries with specific activities, timings, and locations
        4. Provide practical travel advice and cultural insights
        5. Suggest authentic local experiences and hidden gems
        
        When creating plans:
        - Be specific about locations, addresses, and timing
        - Include realistic travel times between activities
        - Consider opening hours and seasonal factors
        - Suggest a mix of must-see attractions and local experiences
        - Provide cost estimates when possible
        - Include cultural context and travel tips
        
        Format your response as a structured plan that includes:
        - Day-by-day itinerary with activities
        - Meal recommendations
        - Transportation suggestions
        - Accommodation options
        - Budget estimates
        - Weather considerations
        - Cultural tips and etiquette
    """.trimIndent()
    
    suspend fun createEnhancedJourneyPlan(request: JourneyPlanRequest): JourneyPlanResponse = coroutineScope {
        // Gather real-time information about the destination
        val searchResultsDeferred = async {
            searchService.searchTravelInfo(request.destination)
        }
        
        // Calculate trip duration
        val startDate = LocalDate.parse(request.startDate)
        val endDate = LocalDate.parse(request.endDate)
        val days = ChronoUnit.DAYS.between(startDate, endDate).toInt() + 1
        
        // Get search results
        val searchResults = searchResultsDeferred.await()
        
        // Create enhanced prompt with real-time data
        val enhancedPrompt = buildEnhancedPrompt(request, searchResults, days)
        
        // TODO: Generate AI response using Koog agent
        // For now, use the GeminiService to create the response
        val response = geminiService.createJourneyPlan(request)
        
        // Return the enhanced response with search data
        response
    }
    
    private fun buildEnhancedPrompt(
        request: JourneyPlanRequest,
        searchResults: List<SearchResult>,
        days: Int
    ): String {
        return buildString {
            appendLine("Create a comprehensive $days-day journey plan for ${request.destination}")
            appendLine("Travel Dates: ${request.startDate} to ${request.endDate}")
            
            request.preferences?.let { prefs ->
                appendLine("\nTraveler Preferences:")
                prefs.budget?.let { appendLine("- Budget Level: $it") }
                prefs.travelStyle?.let { appendLine("- Travel Style: $it") }
                prefs.accommodation?.let { appendLine("- Accommodation Preference: $it") }
                prefs.transportation?.let { appendLine("- Transportation Preference: $it") }
                if (prefs.interests.isNotEmpty()) {
                    appendLine("- Interests: ${prefs.interests.joinToString(", ")}")
                }
            }
            
            appendLine("\nCurrent Information about ${request.destination}:")
            searchResults.take(10).forEach { result ->
                appendLine("- ${result.title}: ${result.snippet}")
            }
            
            appendLine("\nPlease create a detailed itinerary that includes:")
            appendLine("- Day-by-day schedule with specific activities and timings")
            appendLine("- Specific locations, addresses, and transportation between them")
            appendLine("- Restaurant recommendations for each meal")
            appendLine("- Realistic activity durations and travel times")
            appendLine("- Cost estimates for activities and meals")
            appendLine("- Cultural insights and local tips")
            appendLine("- Weather considerations for the travel dates")
            appendLine("- Accommodation suggestions")
            
            appendLine("\nStructure the response to be clear and actionable for the traveler.")
        }
    }
    
    private suspend fun parseEnhancedJourneyPlan(
        aiResponse: String,
        request: JourneyPlanRequest,
        days: Int
    ): JourneyPlanResponse {
        val startDate = LocalDate.parse(request.startDate)
        val itinerary = mutableListOf<DayPlan>()
        
        // Create detailed itinerary for each day
        for (dayNum in 1..days) {
            val currentDate = startDate.plusDays((dayNum - 1).toLong())
            val formattedDate = currentDate.format(DateTimeFormatter.ISO_LOCAL_DATE)
            
            // Generate activities based on AI response and search data
            val activities = generateActivitiesForDay(dayNum, request.destination, request.preferences)
            val meals = generateMealsForDay(dayNum, request.destination)
            
            val dayPlan = DayPlan(
                day = dayNum,
                date = formattedDate,
                activities = activities,
                meals = meals,
                accommodation = if (dayNum == 1) generateAccommodationSuggestion(request.destination, request.preferences) else null,
                notes = extractDayNotes(aiResponse, dayNum)
            )
            
            itinerary.add(dayPlan)
        }
        
        return JourneyPlanResponse(
            destination = request.destination,
            dateRange = "${request.startDate} to ${request.endDate}",
            itinerary = itinerary,
            travelTips = extractTravelTips(aiResponse, request.destination),
            estimatedBudget = extractBudgetEstimate(aiResponse, request.preferences?.budget),
            weatherInfo = extractWeatherInfo(aiResponse, request.startDate, request.endDate)
        )
    }
    
    private suspend fun generateActivitiesForDay(
        day: Int, 
        destination: String, 
        preferences: JourneyPreferences?
    ): List<Activity> {
        // Generate diverse activities based on day and preferences
        val baseActivities = when (day) {
            1 -> listOf(
                Activity(
                    time = "09:00",
                    name = "Arrival and City Orientation",
                    description = "Explore the main city center and get oriented",
                    location = "City Center, $destination",
                    duration = "2-3 hours",
                    estimatedCost = "Free",
                    category = "orientation"
                ),
                Activity(
                    time = "14:00",
                    name = "Visit Main Attraction",
                    description = "Explore the most famous landmark or attraction",
                    location = "Historic District, $destination",
                    duration = "2-3 hours",
                    estimatedCost = "$25-40",
                    category = "sightseeing"
                )
            )
            2 -> listOf(
                Activity(
                    time = "08:30",
                    name = "Cultural Experience",
                    description = "Visit museums or cultural sites",
                    location = "Cultural Quarter, $destination",
                    duration = "3-4 hours",
                    estimatedCost = "$30-50",
                    category = "culture"
                ),
                Activity(
                    time = "15:00",
                    name = "Local Market Tour",
                    description = "Explore local markets and try street food",
                    location = "Local Market, $destination",
                    duration = "2 hours",
                    estimatedCost = "$20-35",
                    category = "food"
                )
            )
            else -> listOf(
                Activity(
                    time = "09:00",
                    name = "Adventure Activity",
                    description = "Outdoor activity or adventure experience",
                    location = "Natural Area near $destination",
                    duration = "4-5 hours",
                    estimatedCost = "$40-80",
                    category = "adventure"
                ),
                Activity(
                    time = "16:00",
                    name = "Relaxation Time",
                    description = "Unwind and enjoy local atmosphere",
                    location = "Popular District, $destination",
                    duration = "2 hours",
                    estimatedCost = "$15-25",
                    category = "relaxation"
                )
            )
        }
        
        return baseActivities
    }
    
    private fun generateMealsForDay(day: Int, destination: String): List<Meal> {
        return listOf(
            Meal(
                time = "breakfast",
                restaurant = "Local Breakfast Spot",
                cuisine = "Local",
                location = "Near accommodation",
                estimatedCost = "$12-18"
            ),
            Meal(
                time = "lunch",
                restaurant = "Traditional Restaurant",
                cuisine = "Regional",
                location = "City Center",
                estimatedCost = "$25-35"
            ),
            Meal(
                time = "dinner",
                restaurant = "Recommended Restaurant",
                cuisine = "Local/International",
                location = "Dining District",
                estimatedCost = "$40-60"
            )
        )
    }
    
    private fun generateAccommodationSuggestion(destination: String, preferences: JourneyPreferences?): String {
        val accommodationType = preferences?.accommodation ?: "hotel"
        return "Recommended $accommodationType in central $destination with good reviews and convenient location"
    }
    
    private fun extractDayNotes(aiResponse: String, day: Int): String {
        return "Day $day exploration with focus on authentic local experiences"
    }
    
    private fun extractTravelTips(aiResponse: String, destination: String): List<String> {
        return listOf(
            "Check visa requirements and passport validity",
            "Research local customs and cultural etiquette",
            "Download offline maps and translation apps",
            "Notify bank of travel plans",
            "Pack according to local weather and activities",
            "Learn basic phrases in local language",
            "Keep copies of important documents",
            "Research local transportation options"
        )
    }
    
    private fun extractBudgetEstimate(aiResponse: String, budgetLevel: String?): String {
        return when (budgetLevel?.lowercase()) {
            "low" -> "Budget-friendly: $50-80 per day"
            "high" -> "Premium experience: $150-250 per day"
            else -> "Mid-range comfort: $100-150 per day"
        }
    }
    
    private fun extractWeatherInfo(aiResponse: String, startDate: String, endDate: String): String {
        return "Check current weather forecast for $startDate to $endDate and pack accordingly. Consider seasonal weather patterns."
    }
    
    fun close() {
        searchService.close()
    }
}