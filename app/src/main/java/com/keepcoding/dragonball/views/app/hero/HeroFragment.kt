package com.keepcoding.dragonball.views.app.hero

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.keepcoding.dragonball.R
import com.keepcoding.dragonball.data.Personaje
import com.keepcoding.dragonball.databinding.FragmentHeroBinding
import com.keepcoding.dragonball.views.app.AppActivity
import com.keepcoding.dragonball.views.app.AppViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HeroFragment(
    private val activity: AppActivity,
    private val sharedProgress: SharedPreferences,
): Fragment() {

    private lateinit var binding: FragmentHeroBinding

    private val viewModel: AppViewModel by activityViewModels()
    
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHeroBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setObservers()
        setListeners()
    }

    private fun setData(hero: Personaje?) {
        hero?.let {
            if (hero.life == 0) {
                activity.showHome()
            }
            
            binding.life.progress = hero.life
            binding.name.text = hero.name
            Glide
                .with(binding.root)
                .load(hero.photo)
                .centerCrop()
                .placeholder(R.drawable.dragon_ball_logo_transparent_png)
                .into(binding.background)
        }
    }

    private fun setObservers() {
        lifecycleScope.launch(Dispatchers.Main) {
            viewModel.heroeSelected.collect{ hero ->
                setData(hero)
            }
        }
    }

    private fun setListeners() {
        binding.back.setOnClickListener {
            activity.showHome()
        }

        binding.less.setOnClickListener {
            viewModel.damage(sharedProgress)
        }

        binding.more.setOnClickListener {
            viewModel.cure(sharedProgress)
        }
    }
}