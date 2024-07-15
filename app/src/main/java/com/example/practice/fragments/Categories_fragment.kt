package com.example.practice.fragments

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.practice.Adapter.FoodAdapter
import com.example.practice.ApiServiceMeal.Meal
import com.example.practice.ApiServiceMeal.MealList
import com.example.practice.Retrofit.RetrofitInstance
import com.example.practice.databinding.FragmentCategoriesFragmentBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FoodListFragment : Fragment() {

    // Declaraciones de variables
    private lateinit var binding: FragmentCategoriesFragmentBinding
    private lateinit var foodAdapter: FoodAdapter
    private var mealList = mutableListOf<Meal>()

    // Método llamado cuando se crea la vista del fragmento
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar y obtener la referencia al binding
        binding = FragmentCategoriesFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Método llamado después de que la vista ha sido creada
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicialización del adapter y configuración del RecyclerView
        foodAdapter = FoodAdapter()
        binding.rvFood.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = foodAdapter
        }

        // Llamada inicial para obtener todas las comidas
        fetchAllFoods()

        // Configuración del campo de búsqueda
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // No se utiliza en este caso
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // No se utiliza en este caso
            }

            override fun afterTextChanged(s: Editable?) {
                s?.let {
                    if (it.isNotEmpty()) {
                        performSearch(it.toString()) // Realizar búsqueda cuando el texto cambia
                    } else {
                        fetchAllFoods() // Obtener todas las comidas si el texto está vacío
                    }
                }
            }
        })
    }

    // Función para obtener todas las comidas
    private fun fetchAllFoods() {
        RetrofitInstance.api.getRandomFood().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.isSuccessful) {
                    val meals = response.body()?.meals ?: emptyList()
                    mealList.clear() // Limpiar lista actual de comidas
                    mealList.addAll(meals) // Agregar todas las comidas obtenidas
                    foodAdapter.submitList(mealList) // Actualizar el RecyclerView con la nueva lista
                } else {
                    // Manejar respuesta no exitosa
                    // Aquí se puede manejar adecuadamente el caso de respuesta no exitosa si es necesario
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                // Manejar error de la llamada
                // Aquí se puede manejar adecuadamente el error de la llamada si es necesario
            }
        })
    }

    // Función para buscar comidas por nombre
    private fun searchFoods(searchQuery: String) {
        RetrofitInstance.api.searchFoods(searchQuery).enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.isSuccessful) {
                    val filteredMeals = response.body()?.meals ?: emptyList()
                    foodAdapter.submitList(filteredMeals) // Mostrar comidas filtradas en el RecyclerView
                } else {
                    // Manejar respuesta no exitosa
                    // Aquí se puede manejar adecuadamente el caso de respuesta no exitosa si es necesario
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                // Manejar error de la llamada
                // Aquí se puede manejar adecuadamente el error de la llamada si es necesario
            }
        })
    }

    // Método para realizar la búsqueda de comidas por nombre
    private fun performSearch(query: String) {
        searchFoods(query) // Llamar a la función de búsqueda de comidas
    }
}
