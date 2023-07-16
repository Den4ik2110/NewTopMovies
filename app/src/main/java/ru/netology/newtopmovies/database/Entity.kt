package ru.netology.newtopmovies.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ru.netology.newtopmovies.data.Country
import ru.netology.newtopmovies.data.Genre
import ru.netology.newtopmovies.data.SequelsAndPrequels

@Entity(tableName = "movies")
class MovieEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "humor")
    val humor: Int,
    @ColumnInfo(name = "music")
    val music: Int,
    @ColumnInfo(name = "dynamic")
    val dynamic: Int,
    @ColumnInfo(name = "image")
    val image: Int,
    @ColumnInfo(name = "dialogs")
    val dialogs: Int,
    @ColumnInfo(name = "heroes")
    val heroes: Int,
    @ColumnInfo(name = "antiheroes")
    val antiheroes: Int,
    @ColumnInfo(name = "story")
    val story: Int,
    @ColumnInfo(name = "drama")
    val drama: Int,
    @ColumnInfo(name = "repeat")
    val repeat: Int,
    @ColumnInfo(name = "rating")
    val rating: Int,
    @ColumnInfo(name = "keySort")
    val keySort: String = "New_Old",
    @ColumnInfo(name = "urlImage")
    val urlImage: String?,
    @ColumnInfo(name = "genre")
    @TypeConverters(GenreConverter::class)
    val genres: List<Genre>?,
    @ColumnInfo(name = "year")
    val year: String?,
    @ColumnInfo(name = "franchise")
    val franchise: String?,
    @ColumnInfo(name = "kinopoiskId")
    val kinopoiskId: Int? = null,
    @ColumnInfo(name = "nameOriginal")
    val nameOriginal: String? = null,
    @ColumnInfo(name = "posterUrl")
    val posterUrl: String? = null,
    @ColumnInfo(name = "coverUrl")
    val coverUrl: String? = null,
    @ColumnInfo(name = "logoUrl")
    val logoUrl: String? = null,
    @ColumnInfo(name = "ratingKinopoisk")
    val ratingKinopoisk: String? = null,
    @ColumnInfo(name = "ratingKinopoiskVoteCount")
    val ratingKinopoiskVoteCount: String? = null,
    @ColumnInfo(name = "ratingImdb")
    val ratingImdb: String? = null,
    @ColumnInfo(name = "ratingImdbVoteCount")
    val ratingImdbVoteCount: String? = null,
    @ColumnInfo(name = "webUrl")
    val webUrl: String? = null,
    @ColumnInfo(name = "filmLength")
    val filmLength: Int? = null,
    @ColumnInfo(name = "description")
    val description: String? = null,
    @ColumnInfo(name = "ratingAgeLimits")
    val ratingAgeLimits: String? = null,
    @ColumnInfo(name = "countries")
    @TypeConverters(CountriesConverter::class)
    val countries: List<Country>? = null,
    @ColumnInfo(name = "sequelsAndPrequels")
    @TypeConverters(SequelsAndPrequelsConverter::class)
    val sequelsAndPrequels: List<SequelsAndPrequels>? = null,
    @ColumnInfo(name = "review")
    val review: String? = null
)

@Entity(tableName = "searchQuery")
class SearchQueryEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "humor")
    val humor: Int,
    @ColumnInfo(name = "music")
    val music: Int,
    @ColumnInfo(name = "dynamic")
    val dynamic: Int,
    @ColumnInfo(name = "image")
    val image: Int,
    @ColumnInfo(name = "dialogs")
    val dialogs: Int,
    @ColumnInfo(name = "heroes")
    val heroes: Int,
    @ColumnInfo(name = "antiheroes")
    val antiheroes: Int,
    @ColumnInfo(name = "story")
    val story: Int,
    @ColumnInfo(name = "drama")
    val drama: Int,
    @ColumnInfo(name = "repeat")
    val repeat: Int,
    @ColumnInfo(name = "rating")
    val rating: Int,
    @ColumnInfo(name = "keySort")
    val keySort: String = "New_Old",
    @ColumnInfo(name = "urlImage")
    val urlImage: String?,
    @ColumnInfo(name = "genre")
    @TypeConverters(GenreConverter::class)
    val genres: List<Genre>?,
    @ColumnInfo(name = "year")
    val year: String?,
    @ColumnInfo(name = "franchise")
    val franchise: String?,
    @ColumnInfo(name = "kinopoiskId")
    val kinopoiskId: Int? = null,
    @ColumnInfo(name = "nameOriginal")
    val nameOriginal: String? = null,
    @ColumnInfo(name = "posterUrl")
    val posterUrl: String? = null,
    @ColumnInfo(name = "coverUrl")
    val coverUrl: String? = null,
    @ColumnInfo(name = "logoUrl")
    val logoUrl: String? = null,
    @ColumnInfo(name = "ratingKinopoisk")
    val ratingKinopoisk: String? = null,
    @ColumnInfo(name = "ratingKinopoiskVoteCount")
    val ratingKinopoiskVoteCount: String? = null,
    @ColumnInfo(name = "ratingImdb")
    val ratingImdb: String? = null,
    @ColumnInfo(name = "ratingImdbVoteCount")
    val ratingImdbVoteCount: String? = null,
    @ColumnInfo(name = "webUrl")
    val webUrl: String? = null,
    @ColumnInfo(name = "filmLength")
    val filmLength: Int? = null,
    @ColumnInfo(name = "description")
    val description: String? = null,
    @ColumnInfo(name = "ratingAgeLimits")
    val ratingAgeLimits: String? = null,
    @ColumnInfo(name = "countries")
    @TypeConverters(CountriesConverter::class)
    val countries: List<Country>? = null,
    @ColumnInfo(name = "sequelsAndPrequels")
    @TypeConverters(SequelsAndPrequelsConverter::class)
    val sequelsAndPrequels: List<SequelsAndPrequels>? = null,
    @ColumnInfo(name = "review")
    val review: String? = null
)
