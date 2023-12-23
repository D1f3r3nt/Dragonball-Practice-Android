package com.keepcoding.dragonball.views.app.hero

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.keepcoding.dragonball.R
import com.keepcoding.dragonball.data.Personaje
import com.keepcoding.dragonball.databinding.FragmentHeroBinding
import com.keepcoding.dragonball.views.app.AppActivity

class HeroFragment(
    private val activity: AppActivity,
    private val hero: Personaje
): Fragment() {

    private lateinit var binding: FragmentHeroBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHeroBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            activity.showHome()
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