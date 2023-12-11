package com.keepcoding.dragonball.network

import com.keepcoding.dragonball.commons.Error
import com.keepcoding.dragonball.commons.GlobalHeaders
import com.keepcoding.dragonball.commons.Response
import com.keepcoding.dragonball.commons.State
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.util.Base64

class NetworkConnection {
    
    val client = OkHttpClient()

    val BASE_URL = "https://dragonball.keepcoding.education"

    val ENDPOINT_LOGIN = "/api/auth/login"
    
    fun getLogin(username: String, password: String): State {
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
               Response(it.string())
            } ?: Error("Empty response")
        } else {
            return Error("Incorrect username or password")
        }
    }

    private fun basicAuth(username: String, password: String): String {
        val credentials = "$username:$password"
        val base64Credentials = Base64.getEncoder().encodeToString(credentials.toByteArray())
        return "Basic $base64Credentials"
    }
}