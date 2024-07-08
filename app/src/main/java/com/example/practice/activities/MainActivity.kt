package com.example.practice.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.example.practice.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Mostrar el ProgressBar inicialmente
        binding.loader.visibility = View.VISIBLE

        // Ocultar el ProgressBar y mostrar el botón después de 3 segundos (3000 milisegundos)
        Handler().postDelayed({
            binding.loader.visibility = View.GONE
            binding.btnStart.visibility = View.VISIBLE }, 2000)

        binding.btnStart.setOnClickListener {
            val intent = Intent (this, Second_activity::class.java)
            startActivity(intent)
        }




    }
}


