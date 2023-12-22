package com.keepcoding.dragonball.views.hero

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.keepcoding.dragonball.R
import com.keepcoding.dragonball.data.Personaje
import com.keepcoding.dragonball.databinding.ActivityHeroBinding
import com.keepcoding.dragonball.views.home.HomeActivity

class HeroActivity: AppCompatActivity() {

    companion object {
        val INFO_HERO = "info_hero"
        fun go(context: Context, hero: Personaje) {
            val intent = Intent(context, HeroActivity::class.java)
            intent.putExtra(INFO_HERO, Gson().toJson(hero))
            context.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityHeroBinding
    private lateinit var hero: Personaje

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHeroBinding.inflate(layoutInflater)
        hero = Gson().fromJson(intent.getStringExtra(INFO_HERO), Personaje::class.java )
        setContentView(binding.root)
        
        binding.life.progress = hero.life
        Glide
            .with(binding.root)
            .load(hero.photo)
            .centerCrop()
            .placeholder(R.drawable.dragon_ball_logo_transparent_png)
            .into(binding.background)
        
        setListeners()
    }

    private fun setListeners() {
        binding.back.setOnClickListener { 
           HomeActivity.go(this)
        }
        
        binding.less.setOnClickListener { 
            Log.wtf("TEST", "DAÑO")
            // TODO logic
        }

        binding.more.setOnClickListener {
            Log.wtf("TEST", "CURA")
            // TODO logic
        }
    }
}