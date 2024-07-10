package com.example.practice.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor

data class FavoriteMeal(
    val idMeal: String,
    val strMeal: String,
    val strMealThumb: String
)

class FavoriteMealRepository(context: Context) {

    private val dbHelper = DatabaseHelper(context)

    fun insertFavoriteMeal(meal: FavoriteMeal) {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put(DatabaseHelper.COLUMN_MEAL_ID, meal.idMeal)
            put(DatabaseHelper.COLUMN_MEAL_NAME, meal.strMeal)
            put(DatabaseHelper.COLUMN_MEAL_THUMB, meal.strMealThumb)
        }
        db.insert(DatabaseHelper.TABLE_NAME, null, values)
    }

    fun getAllFavoriteMeals(): List<FavoriteMeal> {
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            DatabaseHelper.TABLE_NAME,
            null, null, null, null, null, null
        )

        val favoriteMeals = mutableListOf<FavoriteMeal>()
        with(cursor) {
            while (moveToNext()) {
                val idMeal = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_MEAL_ID))
                val strMeal = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_MEAL_NAME))
                val strMealThumb = getString(getColumnIndexOrThrow(DatabaseHelper.COLUMN_MEAL_THUMB))
                favoriteMeals.add(FavoriteMeal(idMeal, strMeal, strMealThumb))
            }
        }
        cursor.close()
        return favoriteMeals
    }
    //Hacemos la funcion igual que para guardar, para borrar las recetas de la seccion de favoritos
    fun deleteFavoriteFood(idMeal: String){
        val db = dbHelper.writableDatabase
        db.delete(DatabaseHelper.TABLE_NAME,"${DatabaseHelper.COLUMN_MEAL_ID} = ?", arrayOf(idMeal))
    }
}
