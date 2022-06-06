package ru.netology.newtopmovies.database

object DataBaseConstants {

    const val TABLE_NAME = "movies"
    const val _ID = "_id"
    const val COLUMN_NAME_TITLE = "title"
    const val COLUMN_NAME_HUMOR = "humor"
    const val COLUMN_NAME_MUSIC = "music"
    const val COLUMN_NAME_DYNAMIC = "dynamic"
    const val COLUMN_NAME_IMAGE = "image"
    const val COLUMN_NAME_DIALOGS = "dialogs"
    const val COLUMN_NAME_HEROES = "heroes"
    const val COLUMN_NAME_ANTIHEROES = "antiheroes"
    const val COLUMN_NAME_STORY = "story"
    const val COLUMN_NAME_DRAMA = "drama"
    const val COLUMN_NAME_REPEAT = "repeat"
    const val COLUMN_NAME_ICON_ID = "icon_id"
    const val DATA_BASE_NAME = "data_base_movie.db"
    const val DATA_BASE_VERSION = 1

    const val SQL_TABLE_CREATE =
        "CREATE TABLE ${TABLE_NAME} (" +
                "${_ID} INTEGER PRIMARY KEY," +
                "${COLUMN_NAME_TITLE} TEXT," +
                "${COLUMN_NAME_HUMOR} INT," +
                "${COLUMN_NAME_MUSIC} INT," +
                "${COLUMN_NAME_DYNAMIC} INT," +
                "${COLUMN_NAME_IMAGE} INT," +
                "${COLUMN_NAME_DIALOGS} INT," +
                "${COLUMN_NAME_HEROES} INT," +
                "${COLUMN_NAME_ANTIHEROES} INT," +
                "${COLUMN_NAME_STORY} INT," +
                "${COLUMN_NAME_DRAMA} INT," +
                "${COLUMN_NAME_REPEAT} INT," +
                "${COLUMN_NAME_ICON_ID} TEXT)"

    const val SQL_TABLE_DELETE = "DROP TABLE IF EXISTS ${TABLE_NAME}"
}