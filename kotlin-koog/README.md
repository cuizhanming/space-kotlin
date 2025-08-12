# AI Journey Planner

An AI-powered travel planning application built with Kotlin using the Koog AI framework, Gemini LLM, and Google Custom Search.

## Features

- **AI-Powered Planning**: Uses Google's Gemini LLM to create personalized travel itineraries
- **Real-time Information**: Integrates Google Custom Search for up-to-date destination data
- **Comprehensive Itineraries**: Provides day-by-day plans with activities, meals, and accommodations
- **Flexible Preferences**: Supports budget levels, travel styles, and personal interests
- **RESTful API**: Clean HTTP API for easy integration

## Technology Stack

- **Framework**: Koog AI (Kotlin AI agent framework)
- **LLM**: Google Gemini 1.5 Pro
- **Search**: Google Custom Search API
- **Server**: Ktor (Kotlin web framework)
- **Language**: Kotlin with Coroutines

## Setup

### Prerequisites

1. Java 17 or higher
2. Kotlin 2.0+
3. API Keys:
   - Google AI Studio API key for Gemini
   - Google Custom Search API key
   - Google Custom Search Engine ID

### Environment Variables

Copy `.env.example` to `.env` and fill in your API keys:

```bash
cp .env.example .env
```

Required environment variables:
- `GEMINI_API_KEY`: Get from [Google AI Studio](https://makersuite.google.com/app/apikey)
- `GOOGLE_SEARCH_API_KEY`: Get from [Google Cloud Console](https://console.developers.google.com/)
- `GOOGLE_SEARCH_ENGINE_ID`: Create at [Google Custom Search](https://cse.google.com/cse/)

### Running the Application

```bash
# Build the project
./gradlew build

# Run the application
./gradlew run
```

The server will start on `http://localhost:8080`

## API Usage

### Create Journey Plan

**POST** `/api/v1/journey/plan`

```json
{
  "destination": "Tokyo, Japan",
  "startDate": "2024-03-15",
  "endDate": "2024-03-20",
  "preferences": {
    "budget": "medium",
    "travelStyle": "cultural",
    "accommodation": "hotel",
    "transportation": "any",
    "interests": ["temples", "food", "museums", "gardens"]
  }
}
```

**Response:**
```json
{
  "destination": "Tokyo, Japan",
  "dateRange": "2024-03-15 to 2024-03-20",
  "itinerary": [
    {
      "day": 1,
      "date": "2024-03-15",
      "activities": [
        {
          "time": "09:00",
          "name": "Arrival and City Orientation",
          "description": "Explore the main city center and get oriented",
          "location": "Shibuya District, Tokyo",
          "duration": "2-3 hours",
          "estimatedCost": "Free",
          "category": "orientation"
        }
      ],
      "meals": [
        {
          "time": "lunch",
          "restaurant": "Traditional Sushi Restaurant",
          "cuisine": "Japanese",
          "location": "Tsukiji Area",
          "estimatedCost": "$25-35"
        }
      ],
      "accommodation": "Recommended hotel in central Tokyo with good reviews",
      "notes": "Day 1 exploration with focus on authentic local experiences"
    }
  ],
  "travelTips": [
    "Check visa requirements and passport validity",
    "Research local customs and cultural etiquette",
    "Download offline maps and translation apps"
  ],
  "estimatedBudget": "Mid-range comfort: $100-150 per day",
  "weatherInfo": "Check current weather forecast and pack accordingly"
}
```

### Health Check

**GET** `/api/v1/journey/health`

Returns service health status.

## Request Parameters

### JourneyPlanRequest

- `destination` (required): Target destination
- `startDate` (required): Trip start date (YYYY-MM-DD)
- `endDate` (required): Trip end date (YYYY-MM-DD)
- `preferences` (optional): Travel preferences

### JourneyPreferences

- `budget`: "low", "medium", "high"
- `travelStyle`: "adventure", "relaxation", "cultural", "family", "business"
- `accommodation`: "hotel", "hostel", "airbnb", "resort", "any"
- `transportation`: "flight", "train", "car", "bus", "any"
- `interests`: Array of interest keywords

## Architecture

The application follows a modular architecture:

```
src/main/kotlin/com/kotlinkoog/journey/
├── Application.kt              # Main application entry point
├── api/
│   └── JourneyRoutes.kt       # REST API routes
├── config/
│   └── AppConfig.kt           # Configuration management
├── models/
│   └── JourneyModels.kt       # Data models
└── services/
    ├── GeminiService.kt       # Gemini LLM integration
    ├── GoogleSearchService.kt # Google Search integration
    └── JourneyPlannerService.kt # Main planning logic
```

## Development

### Build

```bash
./gradlew build
```

### Test

```bash
./gradlew test
```

### Run in Development

```bash
./gradlew run
```

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a pull request

## License

MIT License