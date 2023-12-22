package com.keepcoding.dragonball.views.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.keepcoding.dragonball.commons.Error
import com.keepcoding.dragonball.commons.ResponseHeroes
import com.keepcoding.dragonball.commons.SharedPreferencesKeys
import com.keepcoding.dragonball.databinding.ActivityHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    companion object {
        fun go(context: Context) {
            val intent = Intent(context, HomeActivity::class.java)
            context.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityHomeBinding
    private lateinit var shared : SharedPreferences
    private lateinit var token: String
    
    private val viewModel: HomeViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        shared = getSharedPreferences(SharedPreferencesKeys.FILE_PREFERENCE, Context.MODE_PRIVATE)
        token = shared.getString(SharedPreferencesKeys.TOKEN, "") ?: ""
        setContentView(binding.root)
        viewModel.getHeroes(token)
        
        setObservers()
    }

    private fun handleErrorState(error: Error) {
        Toast.makeText(this, error.msg, Toast.LENGTH_LONG).show()
    }

    private fun handleResponseHeroesState(response: ResponseHeroes) {
        Log.wtf("TEST", response.heroes.count().toString())
    }

    private fun setObservers() {
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.state.collect{ state ->
                when (state) {
                    is Error -> handleErrorState(state)
                    is ResponseHeroes -> handleResponseHeroesState(state)
                    else -> {}
                }
            }
        }
    }
}