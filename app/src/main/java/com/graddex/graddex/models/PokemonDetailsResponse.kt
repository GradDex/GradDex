package com.graddex.graddex.models

data class PokemonAbilities(val ability: PokemonResponseResult, val is_hidden: Boolean)

data class PokemonTypes(val slot: Int, val type: PokemonResponseResult)

data class PokemonSprites(val back_default: String, val front_default: String)

data class PokemonDetailsResponse(
    val abilities: List<PokemonAbilities>, val name: String, val types: List<PokemonTypes>,
    val species: PokemonResponseResult, val sprites: PokemonSprites
)

data class PokemonDetails(
        var frontSprite: String, val backSprite: String, val name: String, val abilities: List<String>,
        val hiddenAbility: String?
)
