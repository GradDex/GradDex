package com.graddex.graddex

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.graddex.graddex.R.string.*
import com.graddex.graddex.models.PokemonDetailsViewModel
import com.graddex.graddex.databinding.FragmentPokemonDetailsBinding

private val pokemonKey: String = "pokemonKey"

class PokemonDetailsFragment(args: Bundle) : Fragment() {

    constructor(id: String) : this(bundleOf(pokemonKey to id))

    private val pokemonId: String = args.getString(pokemonKey, "")

    @SuppressLint("SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.d(tag, "PokemonID: $pokemonId")

        // Inflate the Pokemon Details fragment layout
        val binding = DataBindingUtil.inflate<FragmentPokemonDetailsBinding>(
            inflater, R.layout.fragment_pokemon_details, container, false
        )

        // Call the Pokemon Details view model for specified Pokemon ID
        val viewModel: PokemonDetailsViewModel = ViewModelProvider(this)
            .get(PokemonDetailsViewModel::class.java)

        binding.statusText.text = getString(loading)
        viewModel.syncPokemonDetails(pokemonId)

        // Update UI with details
        viewModel.pokemonSpriteFront.observe(viewLifecycleOwner,
            { pokemonSpriteFront ->
                binding.pokemonFrontImage.load(pokemonSpriteFront)
                binding.statusText.visibility = View.GONE
            })
        viewModel.pokemonSpriteBack.observe(viewLifecycleOwner,
            { pokemonSpriteBack -> binding.pokemonBackImage.load(pokemonSpriteBack) })
        viewModel.pokemonName.observe(viewLifecycleOwner,
            { pokemonName -> binding.pokemonName.text = pokemonName.toString().capitalize() })
        viewModel.pokemonTypes.observe(viewLifecycleOwner,
            { pokemonTypes ->
                val pokemonTypeList = mutableListOf<String>()
                for (n in 1 .. pokemonTypes.size) {
                    pokemonTypeList += pokemonTypes[n-1].type.name.capitalize()
                }
                binding.pokemonTypes.text = pokemonTypeList.joinToString(" | ")
            })
        viewModel.pokemonAbilities.observe(viewLifecycleOwner,
            { pokemonAbilities ->
                Log.d(tag, "Pokemon Abilities: $pokemonAbilities")
                val pokemonAbilitiesList = mutableListOf<String>()
                for (n in 1 .. pokemonAbilities.size) {
                    pokemonAbilitiesList += pokemonAbilities[n-1].ability.name.capitalize()
                }
                if (pokemonAbilitiesList.size > 1) {
                    binding.pokemonAbilitiesHeader.text = "Abilities:"
                } else { binding.pokemonAbilitiesHeader.text = "Ability:"
                }
                binding.pokemonAbilities.text = pokemonAbilitiesList.joinToString(" | " )
            })

    return binding.root

}

}