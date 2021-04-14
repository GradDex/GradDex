package com.graddex.graddex.models

import com.squareup.moshi.Json

data class PokemonAbilities(
    @Json(name = "ability") val ability: PokemonResponseResult,
    @Json(name = "is_hidden") val is_hidden: Boolean
)

data class PokemonTypes(
    @Json(name = "slot") val slot: Int,
    @Json(name = "type") val type: PokemonResponseResult,
)

data class PokemonSprites(
    @Json(name = "back_default") val back_default: String,
    @Json(name = "front_default") val front_default: String,
)

data class PokemonDetailsResponse(
    @Json(name = "abilities") val abilities: List<PokemonAbilities>,
    @Json(name = "name") val name: String,
    @Json(name = "types") val types: List<PokemonTypes>,
    @Json(name = "species") val species: PokemonResponseResult,
    @Json(name = "sprites") val sprites: PokemonSprites
)

data class PokemonDetails(
    @Json(name = "frontSprite") var frontSprite: String,
    @Json(name = "backSprite") val backSprite: String,
    @Json(name = "name") val name: String,
    @Json(name = "abilities") val abilities: List<String>,
    @Json(name = "hiddenAbility") val hiddenAbility: String?
)
