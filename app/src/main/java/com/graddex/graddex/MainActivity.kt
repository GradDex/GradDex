package com.graddex.graddex

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentContainerView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.graddex.graddex.models.PokemonResponse
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.*
import java.io.File
import java.io.IOException

class MainActivity : AppCompatActivity() {

    val tag = "PokeAPI"

    private lateinit var statusText: TextView
    private lateinit var pokemonRecyclerView: RecyclerView
    private lateinit var pokemonAdapter: PokemonRecyclerAdapter
    private lateinit var pokemonListContainer: ConstraintLayout
    private lateinit var pokemonDetailsContainer: FragmentContainerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusText = findViewById(R.id.status_text)
        pokemonRecyclerView = findViewById(R.id.pokemon_recyclerview)
        pokemonRecyclerView.layoutManager = LinearLayoutManager(this)
        pokemonListContainer = findViewById(R.id.pokemon_list)
        pokemonDetailsContainer = findViewById(R.id.pokemon_details_fragment)

        supportFragmentManager.addOnBackStackChangedListener {
            Log.d(tag, "Back Stack: ${supportFragmentManager.backStackEntryCount}")
            if (supportFragmentManager.backStackEntryCount > 0) {
                pokemonDetailsContainer.isVisible = true
                pokemonListContainer.isVisible = false
            } else {
                pokemonDetailsContainer.isVisible = false
                pokemonListContainer.isVisible = true
            }
        }

        pokemonAdapter = PokemonRecyclerAdapter { name: String, url: String ->
            val transaction = supportFragmentManager.beginTransaction()
            transaction.add(R.id.pokemon_details_fragment, PokemonDetailsFragment(name, url))
                .addToBackStack(null)
                .commit()
        }
        pokemonRecyclerView.adapter = pokemonAdapter

        // Initialise rest client with cache and implement a JSON adapter
        val cache = Cache(File(application.cacheDir, "http_cache"),
                50L * 1024L * 1024L)
        val client = OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor(ChuckerInterceptor(this))
            .build()
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val adapter: JsonAdapter<PokemonResponse> = moshi.adapter(PokemonResponse::class.java)

        // Build the request
        val request = Request.Builder()
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
                    statusText.text = getString(R.string.error_list)
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
