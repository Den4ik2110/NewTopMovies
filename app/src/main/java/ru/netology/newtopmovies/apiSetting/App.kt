package ru.netology.newtopmovies.apiSetting

import android.app.Application
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class App : Application() {
    private lateinit var retrofit: Retrofit

    override fun onCreate() {
        super.onCreate()

        retrofit = Retrofit.Builder()
            .baseUrl("https://kinopoiskapiunofficial.tech/")     // Базовая часть адреса
            .addConverterFactory(GsonConverterFactory.create())  // Конвертер, необходимый для преобразования JSON'а в объекты
            .build()
        kinopoiskApi =
            retrofit.create(KinopoiskAPI::class.java)            // Создаем объект, при помощи которого будем выполнять запросы
    }

    companion object {
        private lateinit var kinopoiskApi: KinopoiskAPI
        fun getApi(): KinopoiskAPI {
            return kinopoiskApi
        }

    }
}