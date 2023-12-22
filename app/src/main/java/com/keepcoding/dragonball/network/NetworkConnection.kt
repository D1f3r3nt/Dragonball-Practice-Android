package com.keepcoding.dragonball.network

import com.google.gson.Gson
import com.keepcoding.dragonball.commons.Error
import com.keepcoding.dragonball.commons.GlobalHeaders
import com.keepcoding.dragonball.commons.ResponseHeroes
import com.keepcoding.dragonball.commons.ResponseToken
import com.keepcoding.dragonball.commons.State
import com.keepcoding.dragonball.data.PersonajeDto
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.util.Base64

class NetworkConnection {

    val BASE_URL = "https://dragonball.keepcoding.education"

    val ENDPOINT_LOGIN = "/api/auth/login"
    val ENDPOINT_HEROES = "/api/heros/all"
    
    fun getLogin(username: String, password: String): State {
        val client = OkHttpClient()
        
        val url = "$BASE_URL$ENDPOINT_LOGIN"

        val request = Request.Builder()
            .url(url)
            .post(FormBody.Builder().build())
            .header(GlobalHeaders.AUTHORIZATION, basicAuth(username, password))
            .build()

        val call = client.newCall(request)
        val response = call.execute()

        if (response.isSuccessful) {
           return response.body?.let {
               ResponseToken(it.string())
            } ?: Error("Empty response")
        } else {
            return Error("Incorrect username or password")
        }
    }
    
    fun getHeroes(token: String): State {
        val client = OkHttpClient()
        
        val url = "$BASE_URL$ENDPOINT_HEROES"
        
        val formBody = FormBody.Builder()
            .add("name", "")
            .build()
        
        val request = Request.Builder()
            .url(url)
            .post(formBody)
            .header(GlobalHeaders.AUTHORIZATION, "Bearer $token")
            .build()

        val call = client.newCall(request)
        val response = call.execute()

        if (response.isSuccessful) {
            return response.body?.let {
                val heroes : Array<PersonajeDto> = Gson().fromJson(it.string(), Array<PersonajeDto>::class.java)
                ResponseHeroes(heroes.toList())
            } ?: Error("Empty response")
        } else {
            return Error("Incorrect token")
        }
    }

    private fun basicAuth(username: String, password: String): String {
        val credentials = "$username:$password"
        val base64Credentials = Base64.getEncoder().encodeToString(credentials.toByteArray())
        return "Basic $base64Credentials"
    }
}