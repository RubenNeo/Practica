package com.example.practice.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.practice.R
import com.example.practice.databinding.ActivityDetailsFoodBinding
import com.example.practice.fragments.home_fragment

class Details_Food_activity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailsFoodBinding
    private lateinit var mealId: String
    private lateinit var mealName: String
    private lateinit var mealPhoto: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailsFoodBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getInformationFromIntent()
        setInformationInViews()
    }

    //Aplicamos la imagen cargada ya con Glide en el imageView mediante la API y la cargamos
    private fun setInformationInViews() {
        Glide.with(applicationContext)
            .load(mealPhoto)
            .into(binding.FoodDetail)
        binding.collapsing.title = mealName
    }

    //Los valores se asignan a las variables mealId, mealName, y mealPhoto que son inicializadas al recibir los datos del Intent
    private fun getInformationFromIntent() {
        val intent = intent
        mealId = intent.getStringExtra(home_fragment.MEAL_ID)!!
        mealName = intent.getStringExtra(home_fragment.MEAL_NAME)!!
        mealPhoto = intent.getStringExtra(home_fragment.MEAL_PHOTO)!!


    }
}