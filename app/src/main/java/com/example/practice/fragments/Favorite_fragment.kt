package com.example.practice.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.practice.Adapter.FavoriteAdapter
import com.example.practice.database.FavoriteMeal
import com.example.practice.database.FavoriteMealRepository
import com.example.practice.databinding.FragmentFavoriteFragmentBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Favorite_fragment : Fragment() {

    private lateinit var binding: FragmentFavoriteFragmentBinding
    private lateinit var favoriteAdapter: FavoriteAdapter
    private lateinit var favoriteMealRepository: FavoriteMealRepository

    // Método para crear la vista del fragmento
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Infla el layout del fragmento usando el data binding
        binding = FragmentFavoriteFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    // Método llamado después de que la vista del fragmento ha sido creada
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inicialización del repositorio de recetas favoritas usando el contexto del fragmento
        favoriteMealRepository = FavoriteMealRepository(requireContext())

        // Configuración del RecyclerView y el adaptador
        setupRecyclerView()

        // Cargar las recetas favoritas al iniciar el fragmento
        loadFavoriteMeals()

        // Configurar el ItemTouchHelper para detectar el deslizamiento para eliminar
        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback { position ->
            val favoriteMeal = favoriteAdapter.currentList[position]
            deleteFavoriteMeal(favoriteMeal)
        })
        itemTouchHelper.attachToRecyclerView(binding.rvFavorites)
    }

    // Método para configurar el RecyclerView
    private fun setupRecyclerView() {
        favoriteAdapter = FavoriteAdapter()
        binding.rvFavorites.apply {
            adapter = favoriteAdapter
            // Configurar el LinearLayoutManager para mostrar las recetas en una lista vertical
            layoutManager = LinearLayoutManager(requireContext())
            // Configurar el GridLayoutManager para mostrar las recetas en una cuadrícula de 2 columnas
            layoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
        }
    }

    // Método para cargar las recetas favoritas desde la base de datos usando corrutinas
    private fun loadFavoriteMeals() {
        GlobalScope.launch(Dispatchers.Main) {
            val favoriteMeals = favoriteMealRepository.getAllFavoriteMeals()
            favoriteAdapter.submitList(favoriteMeals) // Actualiza el RecyclerView con las recetas favoritas
        }
    }

    // Clase interna que implementa SimpleCallback para manejar el deslizamiento para eliminar
    class SwipeToDeleteCallback(private val onDelete : (Int)->Unit) : ItemTouchHelper.SimpleCallback(
        0,
        ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
    ) {
        // No se utiliza el método onMove, devuelve false
        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            return false
        }

        // Método llamado cuando se desliza un elemento hacia la izquierda o derecha
        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            val position = viewHolder.adapterPosition
            onDelete(position) // Llama a la función onDelete definida en el constructor

            // Muestra un Toast indicando que se eliminó la receta de favoritos
            val context = viewHolder.itemView.context
            Toast.makeText(context, "Removed from favorites", Toast.LENGTH_SHORT).show()
        }
    }

    // Método para eliminar una receta de favoritos
    private fun deleteFavoriteMeal(favoriteMeal: FavoriteMeal){
        GlobalScope.launch(Dispatchers.IO) {
            favoriteMealRepository.deleteFavoriteFood(favoriteMeal.idMeal)
        }
    }
}
