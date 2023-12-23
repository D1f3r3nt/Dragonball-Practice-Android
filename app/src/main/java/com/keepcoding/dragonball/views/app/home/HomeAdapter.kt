package com.keepcoding.dragonball.views.app.home

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.keepcoding.dragonball.R
import com.keepcoding.dragonball.data.Personaje
import com.keepcoding.dragonball.databinding.ItemHomeBinding
import com.keepcoding.dragonball.views.app.AppRouter

class HomeAdapter(val activity: AppRouter): RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    private var heroes = emptyList<Personaje>()
    
    class HomeViewHolder(private val  binding: ItemHomeBinding, val activity: AppRouter): RecyclerView.ViewHolder(binding.root) {
        
        fun show(hero: Personaje) {
            binding.name.text = hero.name
            
            if (hero.life == 0) {
                binding.name.setBackgroundResource(R.color.grey)
            } else {
                binding.name.setBackgroundResource(R.color.black_orange)
            }
            
            Glide
                .with(binding.root)
                .load(hero.photo)
                .centerCrop()
                .placeholder(R.drawable.dragon_ball_logo_transparent_png)
                .into(binding.background)
            
            binding.root.setOnClickListener {
                if (hero.life > 0) {
                    activity.showHero(hero)
                } else {
                    Toast.makeText(activity.getContext(), "No tiene vida", Toast.LENGTH_LONG).show()
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        return HomeViewHolder(
            ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            activity
        )
    }

    override fun getItemCount(): Int {
        return heroes.count()
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.show(heroes[position])
    }

    fun update(heroes: List<Personaje>) {
        this.heroes = heroes
        notifyDataSetChanged()
    }

}