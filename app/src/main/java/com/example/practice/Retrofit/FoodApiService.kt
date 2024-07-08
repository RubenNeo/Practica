package com.example.practice.Retrofit

import com.example.practice.ApiServiceMeal.MealList
import retrofit2.Call
import retrofit2.Callback
import retrofit2.http.GET

interface FoodApiService {


//Llamamos a la imagen cualquiera de la API
    @GET ("random.php")
    fun getRandomFood():Call<MealList>

}