package fr.epita.weatherappzainsubhani

import fr.epita.weatherappzainsubhani.WeatherTypeModel
import fr.epita.weatherappzainsubhani.WeatherValuesModel

data class WeatherAPIResponse(
    val id: Int,
    val name: String,
    val coord: CoordinatesModel,
    val weather: List<WeatherTypeModel>,
    val main: WeatherValuesModel
)