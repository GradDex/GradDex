package com.graddex.graddex

import android.util.Log
import com.graddex.graddex.models.PokemonDetailsResponse
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

interface PokemonService {
    suspend fun getPokemonDetails(pokemonName: String): PokemonDetailsResponse
    suspend fun getSpecies(speciesName: String): Unit
}

class PokemonServiceImpl : PokemonService {

    interface PokeAPI {
        @GET("pokemon/{pokemonName}")
        suspend fun getPokemonDetails(@Path("pokemonName") pokemonName: String): PokemonDetailsResponse
    }

    private val httpLogTag = "NetworkLogs"

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://pokeapi.co/api/v2/")
        .client(
            OkHttpClient.Builder()
                .addNetworkInterceptor(
                    HttpLoggingInterceptor(
                        object : HttpLoggingInterceptor.Logger {
                            override fun log(message: String) {
                                Log.d(httpLogTag, message)
                            }
                        }
                    ).setLevel(HttpLoggingInterceptor.Level.BODY)
                )
                .build()
        )
        .addConverterFactory(
            MoshiConverterFactory.create(
                Moshi.Builder()
                    .add(KotlinJsonAdapterFactory())
                    .build()
            )
        )
        .build()


    private val service = retrofit.create(PokeAPI::class.java)

    override suspend fun getPokemonDetails(pokemonName: String): PokemonDetailsResponse =
        service.getPokemonDetails(pokemonName)

    override suspend fun getSpecies(speciesName: String): Unit = TODO()
}
