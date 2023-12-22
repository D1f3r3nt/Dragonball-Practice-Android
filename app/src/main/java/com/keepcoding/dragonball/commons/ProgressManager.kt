package com.keepcoding.dragonball.commons

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.keepcoding.dragonball.data.Personaje

class ProgressManager {
    
    fun controlData(response: ResponseHeroes, sharedProgress: SharedPreferences): List<Personaje> {
        val apiHeroes = mapperToPersonaje(response)

        val realHeroes = chooseData(apiHeroes, sharedProgress)

        deleteBDD(sharedProgress)

        fillBDD(realHeroes, sharedProgress)
        
        return realHeroes
    }

    private fun fillBDD(realHeroes: List<Personaje>, sharedProgress: SharedPreferences) {
        realHeroes.forEach { personaje ->
            sharedProgress.edit().apply {
                putString(personaje.id, Gson().toJson(personaje))
                apply()
            }
        }
    }

    private fun deleteBDD(sharedProgress: SharedPreferences) {
        sharedProgress.edit { clear() }
    }

    private fun chooseData(apiHeroes: List<Personaje>, sharedProgress: SharedPreferences) = apiHeroes.map { personaje ->
        val jsonPersonaje = sharedProgress.getString(personaje.id, Gson().toJson(personaje))
        Gson().fromJson(jsonPersonaje, Personaje::class.java)
    }

    private fun mapperToPersonaje(response: ResponseHeroes): List<Personaje> {
        return response.heroes.map {
            Personaje(it.name, it.photo, it.description, it.favorite, it.id, 100, 100)
        }
    }
    
}