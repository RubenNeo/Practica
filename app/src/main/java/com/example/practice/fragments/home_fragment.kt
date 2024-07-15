package com.example.practice.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.practice.Adapter.CategoriesAdapter
import com.example.practice.ApiServiceMeal.Category
import com.example.practice.ApiServiceMeal.CategoryList
import com.example.practice.ApiServiceMeal.Meal
import com.example.practice.ApiServiceMeal.MealList
import com.example.practice.Retrofit.RetrofitInstance
import com.example.practice.activities.CategoryListActivity
import com.example.practice.activities.Details_Food_activity
import com.example.practice.databinding.FragmentHomeFragmentBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class home_fragment : Fragment() {

    // Declaración de variables
    private lateinit var binding: FragmentHomeFragmentBinding
    private lateinit var categoriesAdapter: CategoriesAdapter

    // Constantes utilizadas para pasar datos entre fragments o actividades
    companion object {
        const val MEAL_ID = "com.example.practice.fragments.MEAL_ID"
        const val MEAL_NAME = "com.example.practice.fragments.MEAL_NAME"
        const val MEAL_PHOTO = "com.example.practice.fragments.MEAL_PHOTO"
        const val CATEGORY_NAME = "com.example.practice.fragments.CATEGORY_NAME"
    }

    // Método llamado cuando se crea la vista del fragmento
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar y obtener la referencia al binding
        binding = FragmentHomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Método llamado después de que la vista ha sido creada
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Preparar y configurar el RecyclerView de categorías
        prepareCategoriesRecyclerView()

        // Cargar comida aleatoria al iniciar el fragmento
        loadRandomFood()
    }

    // Función para manejar el click en una categoría del RecyclerView
    private fun onCategoryClick(category: Category) {
        // Navegar a la actividad CategoryListActivity y pasar el nombre de la categoría
        val intent = Intent(activity, CategoryListActivity::class.java)
        intent.putExtra(CATEGORY_NAME, category.strCategory)
        startActivity(intent)
    }

    // Función para preparar el RecyclerView de categorías
    private fun prepareCategoriesRecyclerView() {
        // Inicializar el adapter de categorías con un lambda para manejar los clicks
        categoriesAdapter = CategoriesAdapter { category ->
            onCategoryClick(category)
        }
        // Configurar el RecyclerView con un GridLayoutManager de 3 columnas
        binding.rvCategories.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = categoriesAdapter
        }
    }

    // Función para cargar una comida aleatoria desde la API
    private fun loadRandomFood() {
        RetrofitInstance.api.getRandomFood().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.isSuccessful && response.body() != null) {
                    // Obtener la primera comida aleatoria de la lista
                    val randomFood: Meal = response.body()!!.meals[0]

                    // Cargar la imagen de la comida aleatoria usando Glide
                    Glide.with(this@home_fragment)
                        .load(randomFood.strMealThumb)
                        .into(binding.randomFood)

                    // Agregar OnClickListener a la imagen para navegar a los detalles
                    binding.randomFood.setOnClickListener {
                        navigateToDetails(randomFood)
                    }
                } else {
                    // Manejar respuesta no exitosa
                    Log.e("Home Fragment", "Failed to get random food")
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                // Manejar error de la llamada
                Log.e("Home Fragment", "Error fetching random food: ${t.message}")
            }
        })
    }

    // Función para navegar a la actividad de detalles de comida
    private fun navigateToDetails(randomFood: Meal) {
        // Crear Intent para pasar datos a Details_Food_activity
        val intent = Intent(activity, Details_Food_activity::class.java).apply {
            putExtra(MEAL_ID, randomFood.idMeal)
            putExtra(MEAL_NAME, randomFood.strMeal)
            putExtra(MEAL_PHOTO, randomFood.strMealThumb)
        }
        startActivity(intent)
    }

    // Función para cargar las categorías desde la API
    private fun loadCategories() {
        RetrofitInstance.api.getItemCategories()?.enqueue(object : Callback<CategoryList> {
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                if (response.isSuccessful && response.body() != null) {
                    // Obtener la lista de categorías
                    val categoryList: List<Category> = response.body()!!.categories
                    // Establecer la lista de categorías en el adapter
                    categoriesAdapter.setCategoryList(categoryList)
                } else {
                    // Manejar respuesta no exitosa
                    Log.e("Home Fragment", "Failed to retrieve categories")
                }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                // Manejar error de la llamada
                Log.e("Home Fragment", "Error fetching categories: ${t.message}")
            }
        })
    }

    // Método llamado cuando el fragmento vuelve a estar visible
    override fun onResume() {
        super.onResume()
        // Cargar las categorías al reanudar el fragmento
        loadCategories()
    }
}
