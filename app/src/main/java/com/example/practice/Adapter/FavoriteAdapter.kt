package com.example.practice.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.practice.database.FavoriteMeal
import com.example.practice.databinding.FoodItemBinding

class FavoriteAdapter(var onFavoriteClick: (FavoriteMeal)-> Unit) :
    ListAdapter<FavoriteMeal, FavoriteAdapter.FavoriteAdapterViewHolder>(FavoriteMealDiffCallback()) {

    // ViewHolder que contiene la vista de cada elemento en el RecyclerView
    inner class FavoriteAdapterViewHolder(private val binding: FoodItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        // Método para vincular los datos de la receta favorita con la vista
        fun bind(favoriteMeal: FavoriteMeal) {
            binding.tvFoodName.text = favoriteMeal.strMeal
            Glide.with(binding.root.context).load(favoriteMeal.strMealThumb).into(binding.ivFood)
        }
    }

    // Método para crear un nuevo ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteAdapterViewHolder {
        val binding = FoodItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FavoriteAdapterViewHolder(binding)
    }

    // Método para vincular los datos de una receta favorita con una posición específica en el RecyclerView
    override fun onBindViewHolder(holder: FavoriteAdapterViewHolder, position: Int) {
        val favoriteMeal = getItem(position)
        holder.bind(favoriteMeal)
        holder.itemView.setOnClickListener {
            onFavoriteClick(favoriteMeal)
        }
    }

    // Clase de utilidad para manejar la diferencia en la lista de elementos
    class FavoriteMealDiffCallback : DiffUtil.ItemCallback<FavoriteMeal>() {
        override fun areItemsTheSame(oldItem: FavoriteMeal, newItem: FavoriteMeal): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(oldItem: FavoriteMeal, newItem: FavoriteMeal): Boolean {
            return oldItem == newItem
        }
    }
}
