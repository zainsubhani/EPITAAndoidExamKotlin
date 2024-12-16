package fr.epita.weatherappzainsubhani
import fr.epita.weatherappzainsubhani.WeatherTypeModel
import fr.epita.weatherappzainsubhani.WeatherValuesModel

data class ForecastValuesModel(
    val dt: Long,
    val main: WeatherValuesModel,
    val weather: List<WeatherTypeModel>,
    val dt_txt: String
)