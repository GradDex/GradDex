package com.graddex.graddex

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject




class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    //get an instance of the client
    private val client = OkHttpClient()

    //build the request
    val request = Request.Builder()
        .cacheControl(CacheControl.Builder().noCache().build())
        .url("https://pokeapi.co/api/v2/pokemon?limit=100&offset=0")
        .build()

    //execute
    val response: Response = client.newCall(request).execute()

    //call request
    val call = client.newCall(request)

    //extract JSON
    val jsonDataString = response.body.toString()
    val json: JSONObject = JSONObject(jsonDataString)


    /**
    fun run() {
        val request = Request.Builder()
            .cacheControl(CacheControl.Builder().noCache().build())
            .url("https://pokeapi.co/api/v2/pokemon?limit=100&offset=0")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            for ((name, value) in response.headers) {
                println("$name: $value")
            }

            println(response.body!!.string())
        } */
    }
