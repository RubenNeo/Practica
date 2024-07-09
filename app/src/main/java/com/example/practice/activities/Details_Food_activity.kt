package com.example.practice.activities


import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.example.practice.ApiServiceMeal.Meal
import com.example.practice.ApiServiceMeal.MealList
import com.example.practice.R
import com.example.practice.Retrofit.RetrofitInstance
import com.example.practice.databinding.ActivityDetailsFoodBinding
import com.example.practice.fragments.home_fragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class Details_Food_activity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsFoodBinding
    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealPhoto: String
    private lateinit var youtubeLink: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getInformationFromIntent()
        setInformationInViews()
        getMealDetails() // Llama a la función para obtener detalles de la comida
        onYoutubeImageLink()//Llamamos a la funcion desde aqui
    }


    //Funcion para que que el ImageView de youtube abra el link hacia esa receta
    private fun onYoutubeImageLink() {
        binding.imgYoutube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
            startActivity(intent)
        }
    }

    //Aplicamos la imagen cargada ya con Glide en el imageView mediante la API y la cargamos
    private fun setInformationInViews() {
        Glide.with(applicationContext)
            .load(mealPhoto)
            .into(binding.FoodDetail)
        binding.collapsing.title = mealName
        binding.collapsing.setCollapsedTitleTextColor(resources.getColor(R.color.black))
        binding.collapsing.setExpandedTitleColor(resources.getColor(R.color.white))
    }


    // Los valores se asignan a las variables mealId, mealName, y mealPhoto que son inicializadas al recibir los datos del Intent
    private fun getInformationFromIntent() {
        val intent = intent
        mealId = intent.getStringExtra(home_fragment.MEAL_ID)!!
        mealName = intent.getStringExtra(home_fragment.MEAL_NAME)!!
        mealPhoto = intent.getStringExtra(home_fragment.MEAL_PHOTO)!!
        Log.d("Food Details", "Meal ID: $mealId, Meal Name: $mealName, Meal Photo: $mealPhoto")
    }

    // Obtenemos los detalles de la comida usando Retrofit y actualizamos la UI
    private fun getMealDetails() {
        RetrofitInstance.api.getRandomFood().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null && response.body()!!.meals.isNotEmpty()) {
                    val meal: Meal = response.body()!!.meals[0]
                    Log.d("Food Details", "Meal details: $meal")
                    updateUI(meal)
                } else {
                    Log.d("Food Details", "Response body is null or empty")
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("Details_Food_activity", "API call failed: ${t.message}")
            }
        })
    }

    private fun updateUI(meal: Meal) {
        binding.CategoryId.text = "Category: ${meal.strCategory}"
        binding.AreaId.text = "Area: ${meal.strArea}"
        binding.InstruccionsId.text = meal.strInstructions
        youtubeLink = meal.strYoutube
    }



}
