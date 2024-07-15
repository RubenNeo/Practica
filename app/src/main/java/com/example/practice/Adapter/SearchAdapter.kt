package com.example.practice.Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.practice.ApiServiceMeal.Meal
import com.example.practice.databinding.FoodItemBinding

class FoodAdapter(var onMealClick:(Meal) -> Unit) : ListAdapter<Meal, FoodAdapter.FoodViewHolder>(FoodDiffCallback()) {

    // Método llamado cuando se necesita crear un nuevo ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        // Inflar el layout de cada elemento de la lista
        val binding = FoodItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FoodViewHolder(binding)
    }

    // Método llamado para actualizar el contenido de un ViewHolder existente
    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        // Vincular los datos del Meal en la posición específica al ViewHolder
        val meal = getItem(position)
        holder.bind(meal)
        holder.itemView.setOnClickListener {
            onMealClick(meal)
        }
    }

    // Clase interna que define el ViewHolder
    inner class FoodViewHolder(private val binding: FoodItemBinding) : RecyclerView.ViewHolder(binding.root) {

        // Método para vincular los datos de un Meal al layout del ViewHolder
        fun bind(meal: Meal) {
            binding.tvFoodName.text = meal.strMeal // Establecer el nombre de la comida
            Glide.with(binding.root) // Cargar la imagen de la comida utilizando Glide
                .load(meal.strMealThumb)
                .into(binding.ivFood)


        }
    }

    // Clase que define el comparador de diferencias para optimizar la actualización de la lista
    class FoodDiffCallback : DiffUtil.ItemCallback<Meal>() {
        // Método para verificar si dos elementos son iguales según su ID
        override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        // Método para verificar si dos elementos contienen los mismos datos
        override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
            return oldItem == newItem
        }
    }
}
