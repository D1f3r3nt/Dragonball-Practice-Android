package com.keepcoding.dragonball.views.app

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keepcoding.dragonball.commons.Idle
import com.keepcoding.dragonball.commons.ProgressManager
import com.keepcoding.dragonball.commons.State
import com.keepcoding.dragonball.data.Personaje
import com.keepcoding.dragonball.network.NetworkConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class AppViewModel: ViewModel() {

    private val networkConnection = NetworkConnection()
    
    private val _state = MutableStateFlow<State>(Idle())
    private val _heroes = MutableStateFlow<List<Personaje>>(emptyList())
    private val _heroeSelected = MutableStateFlow<Personaje?>(null)

    val state: StateFlow<State> = _state
    val heroes: StateFlow<List<Personaje>> = _heroes
    val heroeSelected: StateFlow<Personaje?> = _heroeSelected
    
    private val progressManager = ProgressManager()
    
    fun getHeroes(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = networkConnection.getHeroes(token)
        }
    }
    
    fun setHeroes(heroes: List<Personaje>) {
        _heroes.value = heroes
    }
    
    fun setHeroeSelected(hero: Personaje?) {
        _heroeSelected.value = hero
    }
    
    fun damage(sharedProgress: SharedPreferences) {
        val personaje = _heroeSelected.value?.copy()
        personaje?.let {
            it.life -= Random.nextInt(10, 61)
        
            if (it.life < 0) {
                it.life = 0
            }

            _heroeSelected.value = personaje
            progressManager.saveOne(sharedProgress, personaje)
        }
    }

    fun cure(sharedProgress: SharedPreferences) {
        val personaje = _heroeSelected.value?.copy()
        personaje?.let {
            it.life += 20

            if (it.life > it.maxLife) {
                it.life = it.maxLife
            }
            _heroeSelected.value = personaje
            progressManager.saveOne(sharedProgress, personaje)
        }
    }

    fun superCure(sharedProgress: SharedPreferences) {
        val heroes = progressManager.controlData(_heroes.value, sharedProgress)
        
        val newHeroes: List<Personaje> = heroes.map { 
            it.life = it.maxLife
            it
        }
        
        progressManager.fillBDD(newHeroes, sharedProgress)
        _heroes.value = newHeroes
    }
}