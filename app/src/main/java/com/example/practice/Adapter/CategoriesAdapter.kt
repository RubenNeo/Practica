package com.example.practice.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.practice.ApiServiceMeal.Category
import com.example.practice.ApiServiceMeal.CategoryList
import com.example.practice.databinding.CategoryFoodBinding

class CategoriesAdapter() : RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {
    private var CategoryList = ArrayList<Category>()

    fun setCategoryList(categoryList: List<Category>) {
        this.CategoryList = categoryList as ArrayList<Category>
        notifyDataSetChanged()
    }

    inner class CategoryViewHolder(val binding: CategoryFoodBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            CategoryFoodBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun getItemCount(): Int {
        return CategoryList.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        Glide.with(holder.itemView)
            .load(CategoryList[position].strCategoryThumb)
            .into(holder.binding.ivCategory)
        holder.binding.tvCategories.text = CategoryList[position].strCategory
    }


}