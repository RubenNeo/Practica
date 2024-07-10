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
    private lateinit var binding: FragmentHomeFragmentBinding
    private lateinit var categoriesAdapter: CategoriesAdapter

    companion object {
        const val MEAL_ID = "com.example.practice.fragments.MEAL_ID"
        const val MEAL_NAME = "com.example.practice.fragments.MEAL_NAME"
        const val MEAL_PHOTO = "com.example.practice.fragments.MEAL_PHOTO"
        const val CATEGORY_NAME = "com.example.practice.fragments.CATEGORY_NAME"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareCategoriesRecyclerView()
        loadRandomFood()
    }
//Hacemos la funcion de que al clickar en cada uno de los elemenos del recycler view nos lleve a las diferentes comidas que tiene cada clase
    private fun onCategoryClick(category: Category) {
        val intent = Intent (activity, CategoryListActivity::class.java)
        intent.putExtra(CATEGORY_NAME, category.strCategory)
        startActivity(intent)
    }

    private fun prepareCategoriesRecyclerView() {
        categoriesAdapter = CategoriesAdapter { category ->
            onCategoryClick(category)
        }
        binding.rvCategories.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = categoriesAdapter
        }
    }

    private fun loadRandomFood() {
        RetrofitInstance.api.getRandomFood().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.isSuccessful && response.body() != null) {
                    val randomFood: Meal = response.body()!!.meals[0]

                    // Cargar imagen aleatoria usando Glide
                    Glide.with(this@home_fragment)
                        .load(randomFood.strMealThumb)
                        .into(binding.randomFood)

                    // Agregar OnClickListener a la imagen aleatoria
                    binding.randomFood.setOnClickListener {
                        navigateToDetails(randomFood)
                    }
                } else {
                    Log.e("Home Fragment", "Failed to get random food")
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.e("Home Fragment", "Error fetching random food: ${t.message}")
            }
        })
    }

    private fun navigateToDetails(randomFood: Meal) {
        val intent = Intent(activity, Details_Food_activity::class.java).apply {
            putExtra(MEAL_ID, randomFood.idMeal)
            putExtra(MEAL_NAME, randomFood.strMeal)
            putExtra(MEAL_PHOTO, randomFood.strMealThumb)
        }
        startActivity(intent)
    }
//Hacemos la llamada igual que el LoadRandomFood pero con categorias que esta en el adapter
    private fun loadCategories() {
        RetrofitInstance.api.getItemCategories()?.enqueue(object : Callback<CategoryList> {
            override fun onResponse(call: Call<CategoryList>, response: Response<CategoryList>) {
                if (response.isSuccessful && response.body() != null) {
                    val categoryList: List<Category> = response.body()!!.categories
                    categoriesAdapter.setCategoryList(categoryList)
                } else {
                    Log.e("TEST", "categories")
                }
            }

            override fun onFailure(call: Call<CategoryList>, t: Throwable) {
                Log.e("TEST", "categories: ${t.message}")
            }
        })
    }

    override fun onResume() {
        super.onResume()
        loadCategories()
    }
}
