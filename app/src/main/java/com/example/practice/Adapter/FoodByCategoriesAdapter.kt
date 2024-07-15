package com.example.practice.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.practice.ApiServiceMeal.CategoryFood
import com.example.practice.databinding.FoodItemBinding

class FoodByCategoriesAdapter(var OnFoodDetails: (CategoryFood)-> Unit) : RecyclerView.Adapter<FoodByCategoriesAdapter.CategoryFoodViewModel>() {

    private var mealList : List<CategoryFood> = emptyList()

    fun setCategoriesList(mealList: List<CategoryFood>) {
        this.mealList = mealList
        notifyDataSetChanged()
    }
    inner class CategoryFoodViewModel(val binding: FoodItemBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryFoodViewModel {
        return CategoryFoodViewModel(
            FoodItemBinding.inflate(LayoutInflater.from(parent.context))
        )
    }

    override fun getItemCount(): Int {
       return mealList.size
    }

    override fun onBindViewHolder(holder: CategoryFoodViewModel, position: Int) {
        val meal = mealList[position]
        Glide.with(holder.itemView)
            .load(meal.strMealThumb)
            .into(holder.binding.ivFood)
        holder.binding.tvFoodName.text = meal.strMeal

        holder.itemView.setOnClickListener {
                OnFoodDetails(meal)
        }

    }
}


