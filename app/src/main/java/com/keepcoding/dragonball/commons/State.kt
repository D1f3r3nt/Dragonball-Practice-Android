package com.keepcoding.dragonball.commons

sealed class State

class Idle: State()
class Error(val msg: String): State()
class Response(val response: String): State()