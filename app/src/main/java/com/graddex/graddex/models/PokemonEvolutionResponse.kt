package com.graddex.graddex.models

data class PokemonEvolutionChainUrl(val url: String?)

data class PokemonSpecies(
        val evolution_chain: PokemonEvolutionChainUrl?,
        val evolves_from_species: PokemonResponseResult?
)

data class PokemonEvolutionChain(
        val evolves_to: List<PokemonEvolutionChain>, val species: PokemonResponseResult
)

data class PokemonEvolutions(val chain: PokemonEvolutionChain)