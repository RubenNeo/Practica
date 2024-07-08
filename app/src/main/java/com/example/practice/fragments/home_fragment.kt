package com.example.practice.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.example.practice.ApiServiceMeal.Meal
import com.example.practice.ApiServiceMeal.MealList
import com.example.practice.R
import com.example.practice.Retrofit.RetrofitInstance
import com.example.practice.activities.Details_Food_activity
import com.example.practice.databinding.FragmentHomeFragmentBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class home_fragment : Fragment() {
    // Añadimos binding a la pantalla principal del fragment
    private lateinit var binding: FragmentHomeFragmentBinding

    companion object {
        const val MEAL_ID = "com.example.practice.fragments.MEAL_ID"
        const val MEAL_NAME = "com.example.practice.fragments.MEAL_NAME"
        const val MEAL_PHOTO = "com.example.practice.fragments.MEAL_PHOTO"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Aqui añadimos el layout inflate del homeFragment
        binding = FragmentHomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Comprobamos que llamamos bien tanto a la ID como al nombre de la API mediante un log.d
        RetrofitInstance.api.getRandomFood().enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                if (response.body() != null) {
                    val randomFood: Meal = response.body()!!.meals[0]

                    // Añadimos el Glide para las imágenes random
                    Glide.with(this@home_fragment)
                        .load(randomFood.strMealThumb)
                        .into(binding.randomFood)

                    // Añadimos OnClickListener a la imagen
                    binding.randomFood.setOnClickListener {
                        val intent = Intent(activity, Details_Food_activity::class.java).apply {
                            putExtra(MEAL_ID, randomFood.idMeal)
                            putExtra(MEAL_NAME, randomFood.strMeal)
                            putExtra(MEAL_PHOTO, randomFood.strMealThumb)
                        }
                        startActivity(intent)
                    }
                } else {
                    return
                }
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.d("Home Fragment", t.message.orEmpty())
            }
        })
    }

}
