package com.graddex.graddex.models

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.*
import java.io.IOException

class PokemonLocations() {
    val locationList: MutableLiveData<List<PokemonLocation>> = MutableLiveData()

    val tag = "PokeAPI Location"
    private data class RawPokemonLocation(val location_area: PokemonLocation)
    private data class PokemonLocationResponse(val locations: List<RawPokemonLocation>)

    // Initialise rest client and implement a JSON adapter
    private val client = OkHttpClient()
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    private val adapter: JsonAdapter<PokemonLocationResponse> = moshi.adapter(
            PokemonLocationResponse::class.java
    )

    fun syncPokemonLocation(url: String) {

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
                Log.d(tag, "Body: $body")
                val detailsRes = adapter.fromJson(body ?: "")
                Log.d(tag, "detailsRes: $detailsRes")
                if (detailsRes != null) {
                    Log.d(tag, "type of locationRes: ${detailsRes::class.simpleName}")
                    val temporaryLocationsList = mutableListOf<PokemonLocation>()
                    for (location in detailsRes.locations) {
                        temporaryLocationsList.add(location.location_area)
                    }
                    locationList.postValue(temporaryLocationsList)
                }
                else {
                    locationList.postValue(emptyList())
                }
            }
        })
    }
}

