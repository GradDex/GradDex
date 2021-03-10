package com.graddex.graddex

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.graddex.graddex.models.PokemonResponse
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.*
import java.io.File
import java.io.IOException
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    val tag = "PokeAPI"

    private lateinit var statusText: TextView
    private lateinit var pokemonRecyclerView: RecyclerView

    private lateinit var pokemonAdapter: PokemonRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusText = findViewById(R.id.status_text)
        pokemonRecyclerView = findViewById(R.id.pokemon_recyclerview)
        pokemonRecyclerView.layoutManager = LinearLayoutManager(this)
        pokemonAdapter = PokemonRecyclerAdapter()
        pokemonRecyclerView.adapter = pokemonAdapter

        // Initialise rest client with cache and implement a JSON adapter
        val cache = Cache(File(application.cacheDir, "http_cache"),
                50L * 1024L * 1024L)
        val client = OkHttpClient.Builder().cache(cache).build()
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val adapter: JsonAdapter<PokemonResponse> = moshi.adapter(PokemonResponse::class.java)

        // Build the request for a cache valid for 30 minutes
        val request = Request.Builder()
                .cacheControl(
                        CacheControl.Builder().maxStale(30, TimeUnit.MINUTES).build())
                .url("https://pokeapi.co/api/v2/pokemon?limit=100&offset=0")
                .build()

        // Execute the request
        statusText.text = getString(R.string.loading)
        Log.d(tag, "Connecting to PokeAPI")
        client.newCall(request).enqueue(object : Callback {
            // If request fails to get a response
            override fun onFailure(call: Call, e: IOException) {
                // Logging details
                Log.d(tag, "Connection to PokeAPI failed")

                // Update UI to show error text
                runOnUiThread {
                    statusText.text = getString(R.string.error)
                }
            }

            // If request successfully gets a response
            override fun onResponse(call: Call, response: Response) {
                // Logging details
                Log.d(tag, "onResponse: ${response.code}")
                val body = response.body?.string()
                Log.d(tag, "onResponse: $body")
                val res: PokemonResponse? = adapter.fromJson(body ?: "")
                Log.d(tag, "onResponse: ${res!!.results[0]}")

                // Update UI to remove the status text and show obtained list of Pokemon
                runOnUiThread {
                    statusText.visibility = View.GONE
                    pokemonRecyclerView.visibility = View.VISIBLE
                    pokemonAdapter.submitList(res.results)
                }
            }
        })
    }

}
