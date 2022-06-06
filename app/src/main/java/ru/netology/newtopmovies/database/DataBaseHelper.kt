package ru.netology.newtopmovies.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DataBaseHelper(context: Context) : SQLiteOpenHelper(context, DataBaseConstants.DATA_BASE_NAME, null, DataBaseConstants.DATA_BASE_VERSION) {

    override fun onCreate(dataBase: SQLiteDatabase?) {
        dataBase?.execSQL(DataBaseConstants.SQL_TABLE_CREATE)
    }

    override fun onUpgrade(dataBase: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        dataBase?.execSQL(DataBaseConstants.SQL_TABLE_DELETE)
        onCreate(dataBase)
    }

}