package com.graddex.graddex

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.graddex.graddex.models.PokemonDetailsViewModel
import com.graddex.graddex.databinding.FragmentPokemonDetailsBinding

private val pokemonKey: String = "pokemonKey"

class PokemonDetailsFragment(args: Bundle) : Fragment() {

    private lateinit var viewModel: PokemonDetailsViewModel

    constructor(id: String) : this(bundleOf(pokemonKey to id))

    private val pokemonId: String = args.getString(pokemonKey, "")

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        // Inflate the Pokemon Details fragment layout
        val binding = DataBindingUtil.inflate<FragmentPokemonDetailsBinding>(
                inflater, R.layout.fragment_pokemon_details, container, false
        )

        // Call the Pokemon Details view model
        viewModel = ViewModelProvider(this).get(PokemonDetailsViewModel::class.java)

        return binding.root

    }

}