package fr.epita.weatherappzainsubhani

import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL

class WeatherDetailsActivity : AppCompatActivity() {

    private lateinit var editTextCityName: EditText
    private lateinit var buttonSearch: Button
    private lateinit var statusText: TextView
    private lateinit var resultContainer: LinearLayout
    private var citiesList = mutableListOf<City>()

    data class City(val id: Int, val name: String)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_details)

        // Initialize views
        editTextCityName = findViewById(R.id.editTextCityName)
        buttonSearch = findViewById(R.id.buttonSearch)
        statusText = findViewById(R.id.statusText)
        resultContainer = findViewById(R.id.resultContainer)

        // Fetch cities using Coroutine
        fetchCitiesFromAPI()

        // Handle search button click
        buttonSearch.setOnClickListener {
            val query = editTextCityName.text.toString().trim()
            if (query.isEmpty()) {
                Toast.makeText(this, "Please enter a city name", Toast.LENGTH_SHORT).show()
            } else {
                searchCities(query)
            }
        }
    }

    private fun searchCities(query: String) {
        // Filter results that include the query
        val partialMatches = citiesList.filter { it.name.contains(query, ignoreCase = true) }

        // Look for exact matches
        val exactMatches = citiesList.filter { it.name.equals(query, ignoreCase = true) }

        // Clear previous results
        resultContainer.removeAllViews()

        when {
            exactMatches.isNotEmpty() -> {
                // Show exact match if available (takes priority when results are too many)
                statusText.text = "${exactMatches.size} exact result(s) found:"
                displayResultButtons(exactMatches)
            }
            partialMatches.isEmpty() -> {
                // No results found
                statusText.text = "No results found for \"$query\"."
            }
            partialMatches.size > 4 -> {
                // Too many results: ask the user to refine the search
                statusText.text = "Too many results. Please refine your search."
            }
            else -> {
                // Show all partial matches if 4 or fewer
                statusText.text = "${partialMatches.size} result(s) found:"
                displayResultButtons(partialMatches)
            }
        }
    }


    private fun displayResultButtons(results: List<City>) {
        for (city in results) {
            val button = Button(this)
            button.text = city.name
            button.layoutParams = LinearLayout.LayoutParams(
                resources.displayMetrics.widthPixels / 2, // 50% of screen width
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply { topMargin = 8 }

            button.setOnClickListener {
                // Open CityWeatherActivity and pass city data
                val intent = Intent(this, CityWeatherActivity::class.java)
                intent.putExtra("CITY_NAME", city.name)
                startActivity(intent)
            }
            resultContainer.addView(button)
        }
    }

    private fun fetchCitiesFromAPI() {
        // Use CoroutineScope for background tasks
        CoroutineScope(Dispatchers.IO).launch {
            val urlString = "https://www.surleweb.xyz/api/weather/cities.json"
            val tempCitiesList = mutableListOf<City>()
            try {
                val url = URL(urlString)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"

                val inputStream = connection.inputStream.bufferedReader().use { it.readText() }
                val jsonArray = JSONArray(inputStream)

                for (i in 0 until jsonArray.length()) {
                    val jsonObject = jsonArray.getJSONObject(i)
                    val id = jsonObject.getInt("city_id")  // Correct field names
                    val name = jsonObject.getString("city_name")
                    tempCitiesList.add(City(id, name))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@WeatherDetailsActivity, "Failed to load cities.", Toast.LENGTH_LONG).show()
                }
            }

            // Update UI on main thread
            withContext(Dispatchers.Main) {
                citiesList.addAll(tempCitiesList)
                buttonSearch.text = "Search ${citiesList.size} cities in DB"
            }
        }
    }
}
