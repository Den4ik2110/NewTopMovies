package ru.netology.newtopmovies.database

import androidx.room.TypeConverter
import ru.netology.newtopmovies.data.Country


class CountriesConverter {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromCountries(countries: List<Country>?): String? {
            var stringCountry = ""
            countries?.forEachIndexed { index, country ->
                stringCountry += if (index < countries.size - 1) country.country + ", " else country.country
            }
            return stringCountry.ifBlank { null }
        }

        @TypeConverter
        @JvmStatic
        fun toCountries(data: String?): List<Country>? {
            val listCountry = mutableListOf<Country>()
            return if (data == null) {
                null
            } else {
                data.trim().split(", ").forEach {
                    listCountry.add(Country(it))
                }
                listCountry.toList()
            }
        }
    }
}