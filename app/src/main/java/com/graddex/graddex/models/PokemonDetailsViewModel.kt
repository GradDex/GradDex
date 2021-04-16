package com.graddex.graddex.models

import android.util.Log
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
    val previousEvolutionDetails = MutableLiveData<EvolutionDetails>()
    private val nextEvolutionDetails = MutableLiveData<EvolutionDetails>()

    private val tag = "PokeAPI Details"

    private val pokemonService: PokemonService = PokemonServiceImpl()

    fun syncPokemonDetails(pokemonName: String) {
        launch(coroutineContext) {

            // Get Pokemon Details
            val detailsRes = pokemonService.getPokemonDetails(pokemonName)
            val speciesRes = pokemonService.getSpeciesDetails(pokemonName)

            val pokemonSpriteFront = detailsRes!!.sprites.front_default
            val pokemonSpriteBack = detailsRes.sprites.back_default
            val pokemonName = detailsRes.name
            val pokemonTypes = detailsRes.types
            val pokemonAbilities = detailsRes.abilities
            val pokemonHiddenAbility = detailsRes.abilities

            pokemonDetails.postValue(
                    PokemonDetails(
                            frontSprite = pokemonSpriteFront,
                            backSprite = pokemonSpriteBack,
                            name = pokemonName
                                    .capitalize(Locale.ROOT),
                            type = pokemonTypes
                                    .joinToString(" and ") { it.type.name.capitalize(Locale.ROOT) },
                            abilities = pokemonAbilities
                                    .filter { !it.is_hidden }
                                    .map { it.ability.name.capitalize(Locale.ROOT) },
                            hiddenAbility = pokemonHiddenAbility
                                    .filter { it.is_hidden }
                                    .map { it.ability.name.capitalize(Locale.ROOT) }
                    )
            )

            // Get previous evolution details
            if (speciesRes.evolves_from_species != null) {
                val previousEvolutionName = speciesRes.evolves_from_species.name
                val previousEvoRes = pokemonService.getPokemonDetails(previousEvolutionName)
                val previousEvolutionSprite = previousEvoRes.sprites.front_default

                previousEvolutionDetails.postValue(
                        EvolutionDetails(
                                sprite = previousEvolutionSprite,
                                name = previousEvolutionName.capitalize(Locale.ROOT)
                        )
                )
            }

            // Get next evolution details
            if (speciesRes.evolution_chain != null) {
                val evolutionID = speciesRes.evolution_chain.url
                        .toString()
                        .removePrefix("https://pokeapi.co/api/v2/evolution-chain/")
                val evolutionRes = pokemonService.getEvolutionChain(evolutionID)
                Log.d(tag, "Evolution Chain Response: $evolutionRes")
                // Find second evolution details
                if (evolutionRes.chain.evolves_to.isNotEmpty()) {
                    val evolutionChain = evolutionRes.chain.evolves_to[0]

                    // If current Pokemon is first in the evolution chain, pull second evolution
                    // details. Else, if the current Pokemon is second in the evolution chain,
                    // check if a third evolution exists and pull details
                    if (evolutionRes.chain.species.name == pokemonName) {
                        nextEvolutionDetails(evolutionChain.species.name)
                    } else if (evolutionChain.species.name == pokemonName &&
                            evolutionChain.evolves_to.isNotEmpty()) {
                        nextEvolutionDetails(evolutionChain.evolves_to[0].species.name)
                    }
                }
            }
        }
    }

    private fun nextEvolutionDetails(nextEvolutionName: String) {
        launch(coroutineContext) {
            val nextEvoRes = pokemonService.getPokemonDetails(nextEvolutionName)
            val nextEvolutionSprite = nextEvoRes.sprites.front_default
            nextEvolutionDetails.postValue(
                    EvolutionDetails(
                            sprite = nextEvolutionSprite,
                            name = nextEvolutionName.capitalize(Locale.ROOT)
                    )
            )
        }
    }

}