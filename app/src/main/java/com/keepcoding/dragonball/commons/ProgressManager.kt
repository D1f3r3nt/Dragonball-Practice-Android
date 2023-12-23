package com.keepcoding.dragonball.commons

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.keepcoding.dragonball.data.Personaje

class ProgressManager {
    
    fun controlData(apiHeroes: List<Personaje>, sharedProgress: SharedPreferences): List<Personaje> {
        // Se mira si ya existe ese personaje, si existe lo devuelve, sino lo crea y devuelve el de la api
        val realHeroes = chooseData(apiHeroes, sharedProgress)

        // Vaciamos BDD y rellenamos con los heroes que han pasado el filtro
        fillBDD(realHeroes, sharedProgress)
        
        // Devolvemos Personajes reales
        return realHeroes
    }

    fun fillBDD(realHeroes: List<Personaje>, sharedProgress: SharedPreferences) {
        sharedProgress.edit { clear() }

        realHeroes.forEach { personaje -> saveOne(sharedProgress, personaje) }
    }

    fun saveOne(sharedProgress: SharedPreferences, personaje: Personaje) {
        sharedProgress.edit().apply {
            putString(personaje.id, Gson().toJson(personaje))
            apply()
        }
    }

    private fun chooseData(apiHeroes: List<Personaje>, sharedProgress: SharedPreferences) = apiHeroes.map { personaje ->
        val jsonPersonaje = sharedProgress.getString(personaje.id, Gson().toJson(personaje))
        Gson().fromJson(jsonPersonaje, Personaje::class.java)
    }
    
}