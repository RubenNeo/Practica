package com.example.practice.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.practice.Adapter.FoodByCategoriesAdapter
import com.example.practice.ApiServiceMeal.FoodByCategories
import com.example.practice.Retrofit.RetrofitInstance
import com.example.practice.databinding.ActivityCategoryListBinding
import com.example.practice.fragments.home_fragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategoryListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategoryListBinding
    private lateinit var foodByCategoriesAdapter: FoodByCategoriesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inflar el layout de la actividad usando el binding generado
        binding = ActivityCategoryListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar el RecyclerView y obtener datos por categoría
        initRecyclerView()
        fetchFoodsByCategory(intent.getStringExtra(home_fragment.CATEGORY_NAME)!!)
    }

    // Función para inicializar el RecyclerView y configurar el adaptador
    private fun initRecyclerView() {
        // Crear una instancia del adaptador personalizado FoodByCategoriesAdapter
        foodByCategoriesAdapter = FoodByCategoriesAdapter { categoryFood ->
            // Manejar el clic en un elemento del RecyclerView aquí
            val intent = Intent(this, Details_Food_activity::class.java).apply {
                putExtra(home_fragment.MEAL_ID, categoryFood.idMeal)
                putExtra(home_fragment.MEAL_NAME, categoryFood.strMeal)
                putExtra(home_fragment.MEAL_PHOTO, categoryFood.strMealThumb)
            }
            startActivity(intent)
        }

        // Configurar el RecyclerView con un GridLayoutManager de 2 columnas
        binding.rvFoodByCategory.apply {
            layoutManager = GridLayoutManager(context, 2)
            adapter = foodByCategoriesAdapter
        }
    }

    // Función para obtener datos por categoría desde la API usando Retrofit
    private fun fetchFoodsByCategory(category: String) {
        // Llamar a la API para obtener las comidas por categoría
        RetrofitInstance.api.getFoodsByCategory(category)
            ?.enqueue(object : Callback<FoodByCategories> {
                override fun onResponse(
                    call: Call<FoodByCategories>,
                    response: Response<FoodByCategories>
                ) {
                    if (response.isSuccessful) {
                        // Actualizar el adaptador con la lista de comidas obtenidas
                        response.body()?.meals?.let {
                            foodByCategoriesAdapter.setCategoriesList(it)
                        }
                    } else {
                        // Manejar algún tipo de respuesta de error si la llamada no fue exitosa
                    }
                }

                override fun onFailure(call: Call<FoodByCategories>, t: Throwable) {
                    // Manejar el error de la llamada a la API
                    t.printStackTrace()
                }
            })
    }
}
