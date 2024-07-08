package com.example.practice.Retrofit

import com.example.practice.ApiServiceMeal.MealList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.GET
import retrofit2.http.Query

interface FoodApiService {


//Llamamos a la imagen cualquiera de la API
    @GET ("random.php")
    fun getRandomFood():Call<MealList>

    @GET ("lookup-php?")
    fun getFoodDetails(@Query("i")id : String) : Call <MealList>
}