package com.keepcoding.dragonball.views.app

import android.content.Context
import com.keepcoding.dragonball.data.Personaje

interface AppRouter {
    
    fun getContext(): Context
    fun showHome()
    fun showHero(hero: Personaje)
}