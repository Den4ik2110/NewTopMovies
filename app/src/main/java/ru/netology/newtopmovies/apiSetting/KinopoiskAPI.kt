package ru.netology.newtopmovies.apiSetting

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

import ru.netology.newtopmovies.data.Movie
import ru.netology.newtopmovies.data.SequelsAndPrequels


interface KinopoiskAPI {
    @GET("api/v2.2/films/{id}")
    fun getDataId(
        @Path("id") resourceId: Int,             // id фильма на Кинопоиске
        @Header("X-API-KEY") credentials: String // токен API
    ): Call<Movie?>

    @GET("api/v2.1/films/{id}/sequels_and_prequels")
    fun getSequelsAndPrequels(
        @Path("id") resourceId: Int,             // id фильма на Кинопоиске
        @Header("X-API-KEY") credentials: String // токен API
    ): Call<List<SequelsAndPrequels>?>
}