package com.graddex.graddex

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.FragmentContainerView

class MainActivity : AppCompatActivity() {

    private lateinit var pokemonListContainer: FragmentContainerView
    private lateinit var pokemonDetailsContainer: FragmentContainerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pokemonListContainer = findViewById(R.id.pokemon_list_fragment)
        pokemonDetailsContainer = findViewById(R.id.pokemon_details_fragment)

        // Open Pokemon list fragment
        val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.pokemon_list_fragment, PokemonListFragment()).commit()

        // Change visibility of Pokemon list and Pokemon details fragments
        supportFragmentManager.addOnBackStackChangedListener {
            if (supportFragmentManager.backStackEntryCount > 0) {
                pokemonDetailsContainer.isVisible = true
                pokemonListContainer.isVisible = false
            } else {
                pokemonDetailsContainer.isVisible = false
                pokemonListContainer.isVisible = true
            }
        }
    }
}
