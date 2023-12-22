package com.keepcoding.dragonball.commons

import com.keepcoding.dragonball.data.PersonajeDto

sealed class State

class Idle: State()
class Error(val msg: String): State()
class ResponseToken(val token: String): State()
class ResponseHeroes(val heroes: List<PersonajeDto>): State()