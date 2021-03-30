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
    val previousEvolution: MutableLiveData<String> = MutableLiveData()
    val previousEvolutionSprite: MutableLiveData<String> = MutableLiveData()
    val secondEvolution: MutableLiveData<String> = MutableLiveData()
    val secondEvolutionUrl: MutableLiveData<String> = MutableLiveData()
    val thirdEvolution: MutableLiveData<String> = MutableLiveData()
    val thirdEvolutionUrl: MutableLiveData<String> = MutableLiveData()
    val evolutionSprite: MutableLiveData<String> = MutableLiveData()

    val tag = "PokeAPI Details"

    // Initialise rest client and implement a JSON adapter
    private val client = OkHttpClient()
    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
    val adapter: JsonAdapter<PokemonDetailsResponse> = moshi.adapter(
            PokemonDetailsResponse::class.java
    )
    val speciesAdapter: JsonAdapter<PokemonSpecies> = moshi.adapter(
            PokemonSpecies::class.java
    )
    val evolutionAdapter: JsonAdapter<PokemonEvolutions> = moshi.adapter(
            PokemonEvolutions::class.java
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

                Log.d(tag, "Pokemon Species: ${detailsRes.species}")
                // Build new request for Pokemon species
                val speciesRequest = Request.Builder().url(detailsRes.species.url).build()

                // Execute the request
                client.newCall(speciesRequest).enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                        Log.d(tag, "Pokemon Species Call Failed")
                    }

                    override fun onResponse(call: Call, response: Response) {
                        val body = response.body?.string()
                        val speciesRes = speciesAdapter.fromJson(body ?: "")
                        Log.d(tag, "Pokemon Species Response: $speciesRes")

                        val evolutionChain = speciesRes?.evolution_chain?.url
                        if (evolutionChain != null) {
                            if (speciesRes.evolves_from_species != null) {
                                previousEvolution.postValue(speciesRes.evolves_from_species.name)

                                // Build new request to pull previous evolution's sprite
                                val previousEvolutionUrl = speciesRes.evolves_from_species.url.replace("-species", "")
                                val previousEvolutionRequest = Request.Builder().url(previousEvolutionUrl).build()

                                // Execute the request
                                client.newCall(previousEvolutionRequest).enqueue(object : Callback {
                                    override fun onFailure(call: Call, e: IOException) {
                                        Log.d(tag, "Previous Evolution Call Failed")
                                    }

                                    override fun onResponse(call: Call, response: Response) {
                                        val body = response.body?.string()
                                        val previousEvolutionRes = adapter.fromJson(body ?: "")
                                        previousEvolutionSprite.postValue(previousEvolutionRes!!.sprites.front_default)
                                    }
                                })
                            }

                            // Build new request for Pokemon evolution chain
                            val evolutionRequest = Request.Builder().url(evolutionChain).build()

                            // Execute the request
                            client.newCall(evolutionRequest).enqueue(object : Callback {
                                override fun onFailure(call: Call, e: IOException) {
                                    Log.d(tag, "Pokemon Evolution Chain Call Failed")
                                }

                                override fun onResponse(call: Call, response: Response) {
                                    val body = response.body?.string()
                                    val evolutionRes = evolutionAdapter.fromJson(body ?: "")
                                    Log.d(tag, "Evolution Response: $evolutionRes")
                                    val evolutionChain = evolutionRes!!.chain.evolves_to[0]
                                    secondEvolution.postValue(evolutionChain!!.species.name)
                                    secondEvolutionUrl.postValue(evolutionChain.species.url)
                                    if (evolutionChain.evolves_to.isNotEmpty()) {
                                        thirdEvolution.postValue(evolutionChain.evolves_to[0]!!.species.name)
                                        thirdEvolutionUrl.postValue(evolutionChain.evolves_to[0]!!.species.url)
                                    }
                                }
                            })
                        }
                    }
                })
            }
        })
    }

    fun getEvolutionSprites(url: String) {
        val editedUrl = url.replace("-species", "")
        // Build the request
        val request = Request.Builder().url(editedUrl).build()

        // Execute the request
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(tag, "Call Failed")
            }
            override fun onResponse(call: Call, response: Response) {
                val body = response.body?.string()
                val res = adapter.fromJson(body ?: "")
                evolutionSprite.postValue(res!!.sprites.front_default)
            }
        })
    }

}
