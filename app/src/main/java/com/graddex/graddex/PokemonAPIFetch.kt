package com.graddex.graddex

import android.app.Application
import android.nfc.Tag
import android.util.Log
import android.view.View
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.graddex.graddex.models.PokemonResponse
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.*
import java.io.File
import java.io.IOException

class PokemonAPIFetch {
    fun fetch(application: Application, tag: String, onSuccess: (res: PokemonResponse) -> Unit, onFailure: () -> Unit){
        // Initialise rest client with cache and implement a JSON adapter
        val cache = Cache(
            File(application.cacheDir, "http_cache"),
            50L * 1024L * 1024L)
        val client = OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor(ChuckerInterceptor(application))
            .build()
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val adapter: JsonAdapter<PokemonResponse> = moshi.adapter(PokemonResponse::class.java)

        // Build the request for a cache valid for 30 minutes
        val request = Request.Builder()
            .url("https://pokeapi.co/api/v2/pokemon?limit=100&offset=0")
            .build()

        client.newCall(request).enqueue(object : Callback {
            // If request fails to get a response
            override fun onFailure(call: Call, e: IOException) {
                // Logging details
                Log.d(tag, "Connection to PokeAPI failed")
                onFailure()

            }

            // If request successfully gets a response
            override fun onResponse(call: Call, response: Response) {
                // Logging details
                Log.d(tag, "onResponse: ${response.code}")
                val body = response.body?.string()
                Log.d(tag, "onResponse: $body")
                val res: PokemonResponse? = adapter.fromJson(body ?: "")
                Log.d(tag, "onResponse: ${res!!.results[0]}")
                onSuccess(res)


            }
        })
    }
}