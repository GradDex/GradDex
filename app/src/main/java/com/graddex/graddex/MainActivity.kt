package com.graddex.graddex

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.graddex.graddex.models.PokemonResponse
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.*
import java.io.IOException


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    val TAG = "test"

    override fun onResume() {
        super.onResume()

        //get an instance of the client
        val client = OkHttpClient()
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
       val adapter: JsonAdapter<PokemonResponse> = moshi.adapter(PokemonResponse::class.java)


        //build the request
        val request = Request.Builder()
            .cacheControl(CacheControl.Builder().noCache().build())
            .url("https://pokeapi.co/api/v2/pokemon?limit=100&offset=0")
            .build()

        //execute
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                TODO("Not yet implemented")
            }

            override fun onResponse(call: Call, response: Response) {

                Log.d(TAG, "onResponse: ${response.code}")
                val body = response.body?.string()
                Log.d(TAG, "onResponse: $body")
                val res = adapter.fromJson(body ?: "")
                Log.d(TAG, "onResponse: ${res!!.results[0]}")

            }
        })
    }

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
