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

    val pokemonSpriteFront: MutableLiveData<String> = MutableLiveData()
    val pokemonSpriteBack: MutableLiveData<String> = MutableLiveData()
    val pokemonName: MutableLiveData<String> = MutableLiveData()
    val pokemonTypes: MutableLiveData<List<PokemonTypes>> = MutableLiveData()
    val pokemonAbilities: MutableLiveData<List<PokemonAbilities>> = MutableLiveData()
    val pokemonLocationEncounters: MutableLiveData<String> = MutableLiveData()

    val tag = "PokeAPI Details"

    // Initialise rest client and implement a JSON adapter
    private val client = OkHttpClient()
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    val adapter: JsonAdapter<PokemonDetailsResponse> = moshi.adapter(
        PokemonDetailsResponse::class.java
    )

    fun syncPokemonDetails(url: String) {

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
                pokemonSpriteFront.postValue(detailsRes!!.sprites.front_default)
                pokemonSpriteBack.postValue(detailsRes.sprites.back_default)
                pokemonName.postValue(detailsRes.name)
                pokemonTypes.postValue(detailsRes.types)
                pokemonAbilities.postValue(detailsRes.abilities)
                pokemonLocationEncounters.postValue(detailsRes.location_area_encounters)

                val pokemonLocations = PokemonLocations()
                pokemonLocations.syncPokemonLocation(detailsRes.location_area_encounters)
                val locationList = pokemonLocations.locationList

            }
        })
    }
}
