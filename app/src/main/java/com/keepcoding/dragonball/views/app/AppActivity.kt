package com.keepcoding.dragonball.views.app

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.keepcoding.dragonball.commons.SharedPreferencesKeys
import com.keepcoding.dragonball.data.Personaje
import com.keepcoding.dragonball.databinding.ActivityAppBinding
import com.keepcoding.dragonball.views.app.hero.HeroFragment
import com.keepcoding.dragonball.views.app.home.HomeFragment

class AppActivity: AppCompatActivity(), AppRouter {

    companion object {
        fun go(context: Context) {
            val intent = Intent(context, AppActivity::class.java)
            context.startActivity(intent)
        }
    }
    
    private lateinit var binding: ActivityAppBinding
    private lateinit var sharedProgress : SharedPreferences
    private lateinit var token: String

    private val viewModel: AppViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAppBinding.inflate(layoutInflater)
        
        val shared = getSharedPreferences(SharedPreferencesKeys.FILE_PREFERENCE, Context.MODE_PRIVATE)
        sharedProgress = getSharedPreferences(SharedPreferencesKeys.FILE_PROGRESS, Context.MODE_PRIVATE)
        token = shared.getString(SharedPreferencesKeys.TOKEN, "") ?: ""
        
        setContentView(binding.root)
        
        viewModel.getHeroes(token)
        
        showHome()
    }

    override fun getContext(): Context {
        return this.baseContext
    }

    override fun showHome() {
        supportFragmentManager
            .beginTransaction()
            .replace(binding.frame.id, HomeFragment(this, sharedProgress))
            .commit()
    }

    override fun showHero(hero: Personaje) {
        supportFragmentManager
            .beginTransaction()
            .replace(binding.frame.id, HeroFragment(this, hero))
            .addToBackStack(null)
            .commit()
    }
}