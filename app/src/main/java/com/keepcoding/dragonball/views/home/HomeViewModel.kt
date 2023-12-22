package com.keepcoding.dragonball.views.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.keepcoding.dragonball.commons.Idle
import com.keepcoding.dragonball.commons.State
import com.keepcoding.dragonball.network.NetworkConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {

    private val networkConnection = NetworkConnection()
    
    private val _state = MutableStateFlow<State>(Idle())

    val state: StateFlow<State> = _state
    
    fun getHeroes(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _state.value = networkConnection.getHeroes(token)
        }
    }
}