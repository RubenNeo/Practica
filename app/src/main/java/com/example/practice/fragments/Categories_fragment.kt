package com.example.practice.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.example.practice.Adapter.CategoriesAdapter
import com.example.practice.ApiServiceMeal.Category
import com.example.practice.ApiServiceMeal.CategoryList
import com.example.practice.Retrofit.RetrofitInstance
import com.example.practice.databinding.FragmentCategoriesFragmentBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class Categories_fragment : Fragment() {
    private lateinit var binding: FragmentCategoriesFragmentBinding
    private lateinit var categoriesAdapter: CategoriesAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCategoriesFragmentBinding.inflate(inflater, container, false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareRecycler()
    }

    fun prepareRecycler() {
        categoriesAdapter = CategoriesAdapter({})
        binding.rvCategoriesFragment.apply {
            layoutManager = GridLayoutManager(context, 3, GridLayoutManager.VERTICAL, false)
            adapter = categoriesAdapter


        }
    }

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


