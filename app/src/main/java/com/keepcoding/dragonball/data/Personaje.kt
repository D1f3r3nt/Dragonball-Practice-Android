package com.keepcoding.dragonball.data

data class Personaje (
    val name: String,
    val photo: String,
    val description: String,
    val favorite: Boolean,
    val id: String,
    var life: Int,
    val maxLife: Int,
)