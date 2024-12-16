package fr.epita.weatherappzainsubhani

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class WeatherDetailsActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_weather_details)

        // Find the Back Button and set click listener
        val backButton = findViewById<Button>(R.id.back_button)
        backButton.setOnClickListener {
            finish() // Close this activity and go back to the previous one
        }
    }
}
