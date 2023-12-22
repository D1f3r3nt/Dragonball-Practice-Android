package com.keepcoding.dragonball.views.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.keepcoding.dragonball.R
import com.keepcoding.dragonball.data.Personaje
import com.keepcoding.dragonball.databinding.ItemHomeBinding

class HomeAdapter(val activity: HomeActivity): RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {

    private var heroes = emptyList<Personaje>()
    
    class HomeViewHolder(private val  binding: ItemHomeBinding, val activity: HomeActivity): RecyclerView.ViewHolder(binding.root) {

        fun show(hero: Personaje) {
            binding.name.text = hero.name
            Glide
                .with(binding.root)
                .load(hero.photo)
                .centerCrop()
                .placeholder(R.drawable.dragon_ball_logo_transparent_png)
                .into(binding.background)
            
            binding.root.setOnClickListener {
                Log.wtf("TEST", "Has tocado ${hero.name}")
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