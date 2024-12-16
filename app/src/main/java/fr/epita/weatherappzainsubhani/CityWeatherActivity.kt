package fr.epita.weatherappzainsubhani

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class CityWeatherActivity : AppCompatActivity() {

    private lateinit var mapImage: ImageView
    private lateinit var satelliteImage: ImageView
    private lateinit var weatherIcon: ImageView
    private lateinit var forecastWeatherIcon: ImageView
    private lateinit var cityNameText: TextView
    private lateinit var currentWeatherDetails: TextView
    private lateinit var forecastWeatherDetails: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_city_weather)

        // Retrieve city data from Intent
        val cityName = intent.getStringExtra("CITY_NAME") ?: "Unknown City"
        val longitude = intent.getDoubleExtra("CITY_LONGITUDE", 0.0)
        val latitude = intent.getDoubleExtra("CITY_LATITUDE", 0.0)

        // Initialize views
        mapImage = findViewById(R.id.mapImage)
        satelliteImage = findViewById(R.id.satelliteImage)
        weatherIcon = findViewById(R.id.weatherIcon)
        forecastWeatherIcon = findViewById(R.id.forecastWeatherIcon)
        cityNameText = findViewById(R.id.cityNameText)
        currentWeatherDetails = findViewById(R.id.currentWeatherDetails)
        forecastWeatherDetails = findViewById(R.id.forecastWeatherDetails)

        // Update city name in the UI
        cityNameText.text = cityName

        // Generate Mapbox URLs for map and satellite images
        val mapboxAccessToken = "YOUR_MAPBOX_ACCESS_TOKEN" // Replace with actual token
        val mapUrl = "https://api.mapbox.com/styles/v1/mapbox/streets-v12/static/$longitude,$latitude,8,0/300x300?access_token=$mapboxAccessToken"
        val satelliteUrl = "https://api.mapbox.com/styles/v1/mapbox/satellite-v9/static/$longitude,$latitude,10,0/300x300?access_token=$mapboxAccessToken"

        // Load map and satellite images using Glide
        Glide.with(this).load(mapUrl).into(mapImage)
        Glide.with(this).load(satelliteUrl).into(satelliteImage)

        // Fetch and display weather data
        fetchWeatherData(cityName)
    }

    private fun fetchWeatherData(cityName: String) {
        // Placeholder OpenWeatherMap API call
        val apiKey = "53a8a8aec552df83627ecda5cdc6f23e"
        val cityId = intent.getIntExtra("CITY_ID", 0)
        val currentWeatherUrl = "https://api.openweathermap.org/data/2.5/weather?id=$cityId&appid=$apiKey&units=metric"

        // Simulate fetching weather data (implement actual API call here)
        val fakeWeatherCondition = "clear sky"
        val fakeTemperature = 25.0
        val fakeFeelsLike = 23.5
        val fakeHumidity = 45

        // Update current weather details
        currentWeatherDetails.text = """
            Weather: $fakeWeatherCondition
            Temperature: $fakeTemperature°C
            Feels like: $fakeFeelsLike°C
            Humidity: $fakeHumidity%
        """.trimIndent()

        // Dynamically load weather icons
        val weatherIconUrl = getWeatherIconUrl(fakeWeatherCondition)
        Glide.with(this).load(weatherIconUrl).into(weatherIcon)

        // Placeholder forecast details for tomorrow
        val forecastWeatherCondition = "partly cloudy"
        val forecastTemperature = 22.0
        val forecastFeelsLike = 20.5
        val forecastHumidity = 50

        forecastWeatherDetails.text = """
            Weather: $forecastWeatherCondition
            Temperature: $forecastTemperature°C
            Feels like: $forecastFeelsLike°C
            Humidity: $forecastHumidity%
        """.trimIndent()

        val forecastIconUrl = getWeatherIconUrl(forecastWeatherCondition)
        Glide.with(this).load(forecastIconUrl).into(forecastWeatherIcon)
    }

    private fun getWeatherIconUrl(condition: String): String {
        // Return an icon URL based on weather condition
        return when (condition.lowercase()) {
            "clear sky" -> "https://openweathermap.org/img/wn/01d@2x.png"
            "partly cloudy" -> "https://openweathermap.org/img/wn/02d@2x.png"
            "clouds" -> "https://openweathermap.org/img/wn/03d@2x.png"
            "light rain" -> "https://openweathermap.org/img/wn/10d@2x.png"
            "rain" -> "https://openweathermap.org/img/wn/09d@2x.png"
            "drizzle" -> "https://openweathermap.org/img/wn/09d@2x.png"
            else -> "https://openweathermap.org/img/wn/50d@2x.png" // Default icon
        }
    }
}
