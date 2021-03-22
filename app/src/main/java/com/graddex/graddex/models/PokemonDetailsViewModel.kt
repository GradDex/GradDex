package com.graddex.graddex.models

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.*
import java.io.IOException

class PokemonDetailsViewModel : ViewModel() {

    val pokemonDetails: MutableLiveData<PokemonDetailsResponse> = MutableLiveData()

    fun syncPokemonDetails(url: String) {

        val tag = "PokeAPI Details"

        // Initialise rest client and implement a JSON adapter
        val client = OkHttpClient()
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val adapter: JsonAdapter<PokemonDetailsResponse> = moshi.adapter(
                PokemonDetailsResponse::class.java)

        // Build the request
        val request = Request.Builder().url(url).build()

        // Execute the request
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(tag, "Call Failed")
            }

            // If request successfully gets a response
            override fun onResponse(call: Call, response: Response) {
                Log.d(tag, "Response: $response")
                val body = response.body?.string()
                Log.d(tag,"Body: $body")
                val detailsRes = adapter.fromJson(body ?: "")
                Log.d(tag, "detailsRes: ${detailsRes?.types}")
                pokemonDetails.postValue(detailsRes)
            }
        })
    }
}
