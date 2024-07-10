package com.example.practice.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
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
        binding = ActivityCategoryListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initRecyclerView()
        fetchFoodsByCategory(intent.getStringExtra(home_fragment.CATEGORY_NAME)!!) // Llamar a la función para obtener datos por categoría
    }

    private fun initRecyclerView() {
        foodByCategoriesAdapter = FoodByCategoriesAdapter()
        binding.rvFoodByCategory.apply {
            layoutManager = LinearLayoutManager(this@CategoryListActivity)
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL,false)
            adapter = foodByCategoriesAdapter
        }
    }

    private fun fetchFoodsByCategory(category: String) {
        RetrofitInstance.api.getFoodsByCategory(category)?.enqueue(object : Callback<FoodByCategories> {
            override fun onResponse(call: Call<FoodByCategories>, response: Response<FoodByCategories>) {
                if (response.isSuccessful) {
                    response.body()?.meals?.let {
                        foodByCategoriesAdapter.setCategoriesList(it)
                    }
                } else {
                   //Poner algun tipo de respuesta
                }
            }

            override fun onFailure(call: Call<FoodByCategories>, t: Throwable) {
                t.printStackTrace()
                //error
            }
        })
    }
}
