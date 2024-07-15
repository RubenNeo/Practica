package com.example.practice.activities

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.practice.ApiServiceMeal.Meal
import com.example.practice.ApiServiceMeal.MealList
import com.example.practice.R
import com.example.practice.Retrofit.RetrofitInstance
import com.example.practice.database.FavoriteMeal
import com.example.practice.database.FavoriteMealRepository
import com.example.practice.databinding.ActivityDetailsFoodBinding
import com.example.practice.fragments.home_fragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class  Details_Food_activity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsFoodBinding
    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealPhoto: String
    private lateinit var youtubeLink: String
    private lateinit var favoriteMealRepository: FavoriteMealRepository

    // Método llamado cuando se crea la actividad
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflar el layout de la actividad usando data binding
        binding = ActivityDetailsFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtener datos del Intent y configurar la vista con esos datos
        getInformationFromIntent()
        setInformationInViews()

        // Obtener detalles de la comida usando Retrofit y actualizar la UI
        getMealDetails()

        // Configurar el botón de favoritos para guardar la receta
        buttonHearth()

        // Inicializar el repositorio de recetas favoritas
        favoriteMealRepository = FavoriteMealRepository(this)

        onYoutubeImageLink()
    }

    // Función para abrir el enlace de YouTube al hacer clic en la imagen
    private fun onYoutubeImageLink() {
        binding.imgYoutube.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeLink))
            startActivity(intent)
        }
    }

    // Cargar la imagen y establecer el título de la receta usando Glide
    private fun setInformationInViews() {
        Glide.with(applicationContext)
            .load(mealPhoto)
            .into(binding.FoodDetail)
        binding.collapsing.title = mealName
        binding.collapsing.setCollapsedTitleTextColor(resources.getColor(R.color.black))
        binding.collapsing.setExpandedTitleColor(resources.getColor(R.color.white))
    }

    // Obtener datos del Intent que contiene el ID, nombre y foto de la receta seleccionada
    private fun getInformationFromIntent() {
        val intent = intent
        mealId = intent.getStringExtra(home_fragment.MEAL_ID)!!
        mealName = intent.getStringExtra(home_fragment.MEAL_NAME)!!
        mealPhoto = intent.getStringExtra(home_fragment.MEAL_PHOTO)!!
        Log.d("Food Details", "Meal ID: $mealId, Meal Name: $mealName, Meal Photo: $mealPhoto")
    }

    // Realizar una llamada a la API usando Retrofit para obtener detalles de la comida
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

    // Actualizar la UI con los detalles obtenidos de la comida
    private fun updateUI(meal: Meal) {
        binding.CategoryId.text = "Category: ${meal.strCategory}"
        binding.AreaId.text = "Area: ${meal.strArea}"
        binding.InstruccionsId.text = meal.strInstructions
        youtubeLink = meal.strYoutube
    }

    // Configurar el botón de favoritos para guardar la receta en la base de datos
    private fun buttonHearth() {
        binding.btnFavorite.setOnClickListener {
            saveFavoriteMeal()
        }
    }

    // Guardar la receta seleccionada como favorita en la base de datos local
    private fun saveFavoriteMeal() {
        val favoriteMeal = FavoriteMeal(mealId, mealName, mealPhoto)

        // Utilizar corrutinas para insertar la receta en la base de datos en el hilo de fondo
        CoroutineScope(Dispatchers.IO).launch {
            favoriteMealRepository.insertFavoriteMeal(favoriteMeal)

            // Mostrar un Toast en el hilo principal indicando que se añadió a favoritos
            withContext(Dispatchers.Main) {
                Toast.makeText(this@Details_Food_activity, "Added To Favorites", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}