package com.graddex.graddex

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.graddex.graddex.models.PokemonDetailsViewModel
import com.graddex.graddex.databinding.FragmentPokemonDetailsBinding
import com.graddex.graddex.models.PokemonDetailsResponse

private val pokemonKey: String = "pokemonKey"

class PokemonDetailsFragment(args: Bundle) : Fragment() {

    constructor(id: String) : this(bundleOf(pokemonKey to id))
    private val pokemonId: String = args.getString(pokemonKey, "")

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View? {

        Log.d(tag, "PokemonID: $pokemonId")

        // Inflate the Pokemon Details fragment layout
        val binding = DataBindingUtil.inflate<FragmentPokemonDetailsBinding>(
                inflater, R.layout.fragment_pokemon_details, container, false
        )

        // Call the Pokemon Details view model for specified Pokemon ID
        val viewModel: PokemonDetailsViewModel = ViewModelProvider(this)
                .get(PokemonDetailsViewModel::class.java)

        binding.statusText.text = getString(R.string.loading)
        viewModel.syncPokemonDetails(pokemonId)
        viewModel.pokemonDetails.observe(viewLifecycleOwner,
            { pokemonDetails ->
                binding.statusText.visibility = View.GONE
                binding.pokemonName.visibility = View.VISIBLE
                Log.d(tag, "pokemonDetails: $pokemonDetails")
                binding.pokemonName.text = pokemonDetails.name
                //binding.pokemonImage.visibility = View.VISIBLE
            })

        return binding.root

    }

}