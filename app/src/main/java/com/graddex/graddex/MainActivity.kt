package com.graddex.graddex

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
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
    private var pokemonAPIFetch : PokemonAPIFetch = PokemonAPIFetch()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusText = findViewById(R.id.status_text)
        pokemonRecyclerView = findViewById(R.id.pokemon_recyclerview)
        pokemonRecyclerView.layoutManager = LinearLayoutManager(this)

        pokemonAdapter = PokemonRecyclerAdapter { id ->
            val transaction = supportFragmentManager.beginTransaction()
            transaction.add(R.id.pokemon_details_fragment, PokemonDetailsFragment(id))
                    .addToBackStack(null)
                    .commit()
        }
        pokemonRecyclerView.adapter = pokemonAdapter
        pokemonAPIFetch.fetch(
                application,
                tag,
                onSuccess = {
                    // Update UI to remove the status text and show obtained list of Pokemon
                    runOnUiThread {
                        statusText.visibility = View.GONE
                        pokemonRecyclerView.visibility = View.VISIBLE
                        pokemonAdapter.submitList(it.results)  }
                },
                onFailure = {
                    // Update UI to show error text
                    runOnUiThread {
                        statusText.text = getString(R.string.error_list)
                    }

                }

        )

        // Execute the request
        statusText.text = getString(R.string.loading)
        Log.d(tag, "Connecting to PokeAPI")

    }
}
