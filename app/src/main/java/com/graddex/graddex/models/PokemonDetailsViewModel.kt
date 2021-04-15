package com.graddex.graddex.models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.graddex.graddex.PokemonService
import com.graddex.graddex.PokemonServiceImpl
import kotlinx.coroutines.*
import java.util.*
import kotlin.coroutines.CoroutineContext

class PokemonDetailsViewModel : ViewModel(), CoroutineScope {

    private val coroutineSupervisor = SupervisorJob()

    override val coroutineContext: CoroutineContext = Dispatchers.IO + coroutineSupervisor

    override fun onCleared() {
        super.onCleared()

        coroutineSupervisor.cancel()
    }

    val pokemonDetails = MutableLiveData<PokemonDetails>()
    val previousEvolutionRes: MutableLiveData<PokemonResponseResult> = MutableLiveData()
    val nextEvolutionRes: MutableLiveData<PokemonResponseResult> = MutableLiveData()

    /**val pokemonSpriteFront: MutableLiveData<String> = MutableLiveData()
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
    val nextEvolution: MutableLiveData<String> = MutableLiveData()
    val nextEvolutionSprite: MutableLiveData<String> = MutableLiveData()**/

    val tag = "PokeAPI Details"

    // Initialise rest client and implement a JSON adapter
//    private val client = OkHttpClient()
//    private val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
//    val adapter: JsonAdapter<PokemonDetailsResponse> = moshi.adapter(
//        PokemonDetailsResponse::class.java
//    )
//    val speciesAdapter: JsonAdapter<PokemonSpecies> = moshi.adapter(
//        PokemonSpecies::class.java
//    )
//    val evolutionAdapter: JsonAdapter<PokemonEvolutions> = moshi.adapter(
//        PokemonEvolutions::class.java
//    )

    private val pokemonService: PokemonService = PokemonServiceImpl()


    fun syncPokemonDetails(pokemonName: String) {
        launch(coroutineContext) {
            val detailsRes = pokemonService.getPokemonDetails(pokemonName)

            val pokemonSpriteFront = detailsRes!!.sprites.front_default
            val pokemonSpriteBack = detailsRes.sprites.back_default
            val pokemonName = detailsRes.name.capitalize(Locale.ROOT)
            val pokemonTypes = detailsRes.types
                    .joinToString(" and ") { it.type.name.capitalize(Locale.ROOT) }
            val pokemonAbilities = detailsRes.abilities
                    .filter { !it.is_hidden }
                    .map { it.ability.name.capitalize(Locale.ROOT) }
            val pokemonHiddenAbility = detailsRes.abilities
                    .filter { it.is_hidden }
                    .map{ it.ability.name.capitalize(Locale.ROOT) }

            // String.format("%s %s", getString(hidden_ability), pokemonHiddenAbility)

            pokemonDetails.postValue(
                    PokemonDetails(
                            frontSprite = pokemonSpriteFront,
                            backSprite = pokemonSpriteBack,
                            name = pokemonName,
                            type = pokemonTypes,
                            abilities = pokemonAbilities,
                            hiddenAbility = pokemonHiddenAbility
                    )
            )

//            val species = pokemonService.getSpecies("")
        }


//        // Build the request
//        val request = Request.Builder().url(url).build()
//
//        // Execute the request
//        client.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                Log.d(tag, "Call Failed")
//            }
//
//            // If request successfully gets a response
//            override fun onResponse(call: Call, response: Response) {
//                Log.d(tag, "Response: $response")
//                val body = response.body?.string()
//                Log.d(tag, "Body: $body")
//                val detailsRes = adapter.fromJson(body ?: "")
//                Log.d(tag, "detailsRes: $detailsRes")
//                val pokemonSpriteFront = detailsRes!!.sprites.front_default
//                val pokemonSpriteBack = detailsRes.sprites.back_default
//                val pokemonName = detailsRes.name.capitalize(Locale.ROOT)
//                val pokemonTypes = mutableListOf<String>()
//                for (element in detailsRes.types) {
//                    pokemonTypes += element.type.name.capitalize(Locale.ROOT)
//                }
//                val pokemonAbilities = mutableListOf<String>()
//                for (element in detailsRes.abilities) {
//                    if (!element.is_hidden) {
//                        pokemonAbilities += element.ability.name.capitalize(Locale.ROOT)
//                    } else {
//                        val pokemonHiddenAbility = element.ability.name.capitalize(Locale.ROOT)
//                    }
//                }
//                /**pokemonSpriteFront.postValue(detailsRes!!.sprites.front_default)
//                pokemonSpriteBack.postValue(detailsRes.sprites.back_default)
//                pokemonName.postValue(detailsRes.name)
//                pokemonTypes.postValue(detailsRes.types)
//                pokemonAbilities.postValue(detailsRes.abilities)**/
//
//                Log.d(tag, "Pokemon Species: ${detailsRes.species}")
//                // Build new request for Pokemon species
//                val speciesRequest = Request.Builder().url(detailsRes.species.url).build()
//
//                // Execute the request
//                client.newCall(speciesRequest).enqueue(object : Callback {
//                    override fun onFailure(call: Call, e: IOException) {
//                        Log.d(tag, "Pokemon Species Call Failed")
//                    }
//
//                    override fun onResponse(call: Call, response: Response) {
//                        val body = response.body?.string()
//                        val speciesRes = speciesAdapter.fromJson(body ?: "")
//                        Log.d(tag, "Pokemon Species Response: $speciesRes")
//
//                        val evolutionChain = speciesRes?.evolution_chain?.url
//                        // Exclude Pokemon with no evolutions
//                        if (evolutionChain != null) {
//                            // Check if Pokemon has a previous evolution
//                            if (speciesRes.evolves_from_species != null) {
//                                val previousEvolution =
//                                    mutableListOf(speciesRes.evolves_from_species.name)
//                                //previousEvolution.postValue(speciesRes.evolves_from_species.name)
//
//                                // Build new request to pull previous evolution's sprite
//                                val previousEvolutionUrl =
//                                    speciesRes.evolves_from_species.url.replace("-species", "")
//                                val previousEvolutionRequest =
//                                    Request.Builder().url(previousEvolutionUrl).build()
//
//                                // Execute the request
//                                client.newCall(previousEvolutionRequest).enqueue(object : Callback {
//                                    override fun onFailure(call: Call, e: IOException) {
//                                        Log.d(tag, "Previous Evolution Call Failed")
//                                    }
//
//                                    override fun onResponse(call: Call, response: Response) {
//                                        val body = response.body?.string()
//                                        val previousEvolutionRes = adapter.fromJson(body ?: "")
//                                        previousEvolution += previousEvolutionRes!!.sprites.front_default
//                                        //previousEvolutionSprite.postValue(previousEvolutionRes!!.sprites.front_default)
//                                    }
//                                })
//                            }
//
//                            // Build new request for Pokemon evolution chain
//                            val evolutionRequest = Request.Builder().url(evolutionChain).build()
//
//                            // Execute the request
//                            client.newCall(evolutionRequest).enqueue(object : Callback {
//                                override fun onFailure(call: Call, e: IOException) {
//                                    Log.d(tag, "Pokemon Evolution Chain Call Failed")
//                                }
//
//                                override fun onResponse(call: Call, response: Response) {
//                                    val body = response.body?.string()
//                                    val evolutionRes = evolutionAdapter.fromJson(body ?: "")
//                                    Log.d(tag, "Evolution Response: $evolutionRes")
//                                    // Find second evolution details
//                                    if (evolutionRes!!.chain.evolves_to.isNotEmpty()) {
//                                        val evolutionNameList = arrayListOf<String>()
//                                        val evolutionUrlList = arrayListOf<String>()
//                                        val evolutionChain = evolutionRes.chain.evolves_to[0]
//                                        evolutionNameList.add(evolutionChain!!.species.name)
//                                        evolutionUrlList.add(evolutionChain.species.url)
//                                        // Check if third evolution exists and pull details
//                                        if (evolutionChain.evolves_to.isNotEmpty()) {
//                                            evolutionNameList.add(evolutionChain.evolves_to[0]!!.species.name)
//                                            evolutionUrlList.add(evolutionChain.evolves_to[0]!!.species.url)
//                                        }
//                                        // Call method to find next evolution
//                                        val nextEvolution = findNextEvolution(
//                                            detailsRes.name,
//                                            evolutionNameList,
//                                            evolutionUrlList
//                                        )
//                                    }
//                                }
//                            })
//                        }
//                    }
//                })
//            }
//        })
    }
//
//    fun findNextEvolution(pokemonName: String, nameList: List<String>, urlList: List<String>) {
//        val n = nameList.indexOf(pokemonName) + 1 // First gives 0, second gives 1, third gives 2
//        if (n < nameList.size) {
//            //nextEvolution.postValue(nameList[n])
//            val nextEvolutionName = nameList[n]
//            getEvolutionSprite(urlList[n])
//
//        }
//    }
//
//    private fun getEvolutionSprite(url: String) {
//        val editedUrl = url.replace("-species", "")
//        // Build the request
//        val request = Request.Builder().url(editedUrl).build()
//
//        // Execute the request
//        client.newCall(request).enqueue(object : Callback {
//            override fun onFailure(call: Call, e: IOException) {
//                Log.d(tag, "Call Failed")
//            }
//
//            override fun onResponse(call: Call, response: Response) {
//                val body = response.body?.string()
//                val res = adapter.fromJson(body ?: "")
//                //nextEvolutionSprite.postValue(res!!.sprites.front_default)
//            }
//        })
//    }
}
