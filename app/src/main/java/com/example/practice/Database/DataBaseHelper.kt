package com.example.practice.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "favorite_meals.db"
        private const val DATABASE_VERSION = 1
        const val TABLE_NAME = "favorite_meals"
        const val COLUMN_ID = "id"
        const val COLUMN_MEAL_ID = "meal_id"
        const val COLUMN_MEAL_NAME = "meal_name"
        const val COLUMN_MEAL_THUMB = "meal_thumb"
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = "CREATE TABLE $TABLE_NAME (" +
                "$COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_MEAL_ID TEXT," +
                "$COLUMN_MEAL_NAME TEXT," +
                "$COLUMN_MEAL_THUMB TEXT)"
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}
