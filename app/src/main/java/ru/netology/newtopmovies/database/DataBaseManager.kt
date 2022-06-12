package ru.netology.newtopmovies.database

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import ru.netology.newtopmovies.Movie

class DataBaseManager(
    context: Context,
) {
    private val myDataBaseHelper = DataBaseHelper(context)
    private lateinit var dataBase: SQLiteDatabase

    fun openDb() {
        dataBase = myDataBaseHelper.writableDatabase
    }

    fun insertToDataBase(
        title: String, humor: Int, music: Int, dynamic: Int, image: Int, dialogs: Int,
        heroes: Int, antiheroes: Int, story: Int, drama: Int, repeat: Int, icon_id: Int
    ) {
        val contentValues = ContentValues()
        contentValues.apply {
            put(DataBaseConstants.COLUMN_NAME_TITLE, title)
            put(DataBaseConstants.COLUMN_NAME_HUMOR, humor)
            put(DataBaseConstants.COLUMN_NAME_MUSIC, music)
            put(DataBaseConstants.COLUMN_NAME_DYNAMIC, dynamic)
            put(DataBaseConstants.COLUMN_NAME_IMAGE, image)
            put(DataBaseConstants.COLUMN_NAME_DIALOGS, dialogs)
            put(DataBaseConstants.COLUMN_NAME_HEROES, heroes)
            put(DataBaseConstants.COLUMN_NAME_ANTIHEROES, antiheroes)
            put(DataBaseConstants.COLUMN_NAME_STORY, story)
            put(DataBaseConstants.COLUMN_NAME_DRAMA, drama)
            put(DataBaseConstants.COLUMN_NAME_REPEAT, repeat)
            put(DataBaseConstants.COLUMN_NAME_ICON_ID, icon_id)
        }
        dataBase.insert(DataBaseConstants.TABLE_NAME, null, contentValues)
    }

    @SuppressLint("Recycle")
    fun getFromDataBase(): MutableList<Movie> {
        val moviesList = mutableListOf<Movie>()
        val cursor = dataBase.query(
            DataBaseConstants.TABLE_NAME, null, null,
            null, null, null, null
        )
        while (cursor.moveToNext()) {
            val idMovie = cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseConstants._ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(DataBaseConstants.COLUMN_NAME_TITLE))
            val humor =
                cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseConstants.COLUMN_NAME_HUMOR))
            val music =
                cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseConstants.COLUMN_NAME_MUSIC))
            val dynamic =
                cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseConstants.COLUMN_NAME_DYNAMIC))
            val image =
                cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseConstants.COLUMN_NAME_IMAGE))
            val dialogs =
                cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseConstants.COLUMN_NAME_DIALOGS))
            val heroes =
                cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseConstants.COLUMN_NAME_HEROES))
            val antiheroes =
                cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseConstants.COLUMN_NAME_ANTIHEROES))
            val story =
                cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseConstants.COLUMN_NAME_STORY))
            val drama =
                cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseConstants.COLUMN_NAME_DRAMA))
            val repeat =
                cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseConstants.COLUMN_NAME_REPEAT))
            val iconId =
                cursor.getInt(cursor.getColumnIndexOrThrow(DataBaseConstants.COLUMN_NAME_ICON_ID))
            val movie = Movie(
                title, humor, music, dynamic, image, dialogs,
                heroes, antiheroes, story, drama, repeat, iconId, idMovie = idMovie
            )
            moviesList.add(movie)
        }
        return moviesList
    }

    fun closeDataBase() {
        myDataBaseHelper.close()
    }

    fun deleteMovie(id: Int) {
        dataBase.delete(
            DataBaseConstants.TABLE_NAME,
            DataBaseConstants._ID + "=?",
            arrayOf(id.toString())
        )
    }

    fun updateMovie(
        title: String, humor: Int, music: Int, dynamic: Int, image: Int, dialogs: Int,
        heroes: Int, antiheroes: Int, story: Int, drama: Int, repeat: Int, icon_id: Int, idMovie: Int
    ) {
        val contentValues = ContentValues()
        contentValues.apply {
            put(DataBaseConstants.COLUMN_NAME_TITLE, title)
            put(DataBaseConstants.COLUMN_NAME_HUMOR, humor)
            put(DataBaseConstants.COLUMN_NAME_MUSIC, music)
            put(DataBaseConstants.COLUMN_NAME_DYNAMIC, dynamic)
            put(DataBaseConstants.COLUMN_NAME_IMAGE, image)
            put(DataBaseConstants.COLUMN_NAME_DIALOGS, dialogs)
            put(DataBaseConstants.COLUMN_NAME_HEROES, heroes)
            put(DataBaseConstants.COLUMN_NAME_ANTIHEROES, antiheroes)
            put(DataBaseConstants.COLUMN_NAME_STORY, story)
            put(DataBaseConstants.COLUMN_NAME_DRAMA, drama)
            put(DataBaseConstants.COLUMN_NAME_REPEAT, repeat)
            put(DataBaseConstants.COLUMN_NAME_ICON_ID, icon_id)
        }
        dataBase.update(DataBaseConstants.TABLE_NAME, contentValues, DataBaseConstants._ID + "=?", arrayOf(idMovie.toString()))

    }


}