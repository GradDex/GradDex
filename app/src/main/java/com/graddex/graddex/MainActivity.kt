package com.graddex.graddex

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.graddex.graddex.models.PokemonResponse
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.*
import java.io.IOException


class MainActivity : AppCompatActivity() {

    val TAG = "test"

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
    }


    override fun onResume() {
        super.onResume()

        //get an instance of the client
        val client = OkHttpClient()
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val adapter: JsonAdapter<PokemonResponse> = moshi.adapter(PokemonResponse::class.java)


        //build the request
        val request = Request.Builder()
            .cacheControl(CacheControl.Builder().noCache().build())
            .url("https://pokeapi.co/api/v2/pokemon?limit=100&offset=0")
            .build()

        statusText.text = "Loading.."
        //execute
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    statusText.text = "Error"
                }
            }

            override fun onResponse(call: Call, response: Response) {
                Log.d(TAG, "onResponse: ${response.code}")
                val body = response.body?.string()
                Log.d(TAG, "onResponse: $body")
                val res: PokemonResponse? = adapter.fromJson(body ?: "")
                Log.d(TAG, "onResponse: ${res!!.results[0]}")

                runOnUiThread {
                    statusText.visibility = View.GONE
                    pokemonRecyclerView.visibility = View.VISIBLE
                    pokemonAdapter.submitList(res.results)
                }
            }
        })
    }

    /**
    fun run() {
    val request = Request.Builder()
    .cacheControl(CacheControl.Builder().noCache().build())
    .url("https://pokeapi.co/api/v2/pokemon?limit=100&offset=0")
    .build()

    client.newCall(request).execute().use { response ->
    if (!response.isSuccessful) throw IOException("Unexpected code $response")

    for ((name, value) in response.headers) {
    println("$name: $value")
    }

    println(response.body!!.string())
    } */
}
