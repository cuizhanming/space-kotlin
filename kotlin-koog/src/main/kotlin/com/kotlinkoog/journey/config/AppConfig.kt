package com.kotlinkoog.journey.config

data class AppConfig(
    val server: ServerConfig,
    val gemini: GeminiConfig,
    val googleSearch: GoogleSearchConfig
)

data class ServerConfig(
    val host: String = "0.0.0.0",
    val port: Int = 8080
)

data class GeminiConfig(
    val apiKey: String
)

data class GoogleSearchConfig(
    val apiKey: String,
    val searchEngineId: String
)

object ConfigLoader {
    fun load(): AppConfig {
        val demoMode = System.getenv("DEMO_MODE")?.toBoolean() ?: false
        
        return AppConfig(
            server = ServerConfig(
                host = System.getenv("SERVER_HOST") ?: "0.0.0.0",
                port = System.getenv("SERVER_PORT")?.toIntOrNull() ?: 8080
            ),
            gemini = GeminiConfig(
                apiKey = System.getenv("GEMINI_API_KEY") 
                    ?: if (demoMode) "demo-key" else throw IllegalStateException("GEMINI_API_KEY environment variable is required")
            ),
            googleSearch = GoogleSearchConfig(
                apiKey = System.getenv("GOOGLE_SEARCH_API_KEY")
                    ?: if (demoMode) "demo-key" else throw IllegalStateException("GOOGLE_SEARCH_API_KEY environment variable is required"),
                searchEngineId = System.getenv("GOOGLE_SEARCH_ENGINE_ID")
                    ?: if (demoMode) "demo-engine-id" else throw IllegalStateException("GOOGLE_SEARCH_ENGINE_ID environment variable is required")
            )
        )
    }
}