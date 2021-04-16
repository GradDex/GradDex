package com.graddex.graddex

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
import kotlin.math.log

private const val POKEMON_NAME: String = "POKEMON_NAME"
private const val POKEMON_URL: String = "POKEMON_URL"

class PokemonDetailsFragment(args: Bundle) : Fragment() {

    constructor(pokemonName: String, pokemonUrl: String) : this(
        bundleOf(
            POKEMON_NAME to pokemonName,
            POKEMON_URL to pokemonUrl,
        )
    )

    private val pokemonName: String = args.getString(POKEMON_NAME, "")
    private val pokemonUrl: String = args.getString(POKEMON_URL, "")

    private lateinit var binding: FragmentPokemonDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.d(tag, "pokemonName: $pokemonName")
        Log.d(tag, "pokemonUrl: $pokemonUrl")

        // Inflate the Pokemon Details fragment layout
        binding = DataBindingUtil.inflate<FragmentPokemonDetailsBinding>(
            inflater, R.layout.fragment_pokemon_details, container, false
        )

        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Call the Pokemon Details view model for specified Pokemon ID
        val viewModel: PokemonDetailsViewModel = ViewModelProvider(this)
            .get(PokemonDetailsViewModel::class.java)

        binding.statusText.text = getString(loading)
        viewModel.syncPokemonDetails(pokemonName)

        // Update UI with details
        viewModel.pokemonDetails.observe(viewLifecycleOwner) { pokemonDetails ->
            binding.statusText.visibility = View.GONE
            binding.pokemonFrontImage.load(pokemonDetails.frontSprite)
            binding.pokemonBackImage.load(pokemonDetails.backSprite)
            binding.pokemonName.text = pokemonDetails.name
            binding.pokemonTypes.text = pokemonDetails.type
            binding.pokemonAbilities.text = resources
                .getQuantityText(R.plurals.abilities, pokemonDetails.abilities.size)
                .toString()
                .plus(pokemonDetails.abilities.joinToString(" and "))
            binding.pokemonHiddenAbility.text = resources
                .getQuantityText(R.plurals.hidden_abilities, pokemonDetails.hiddenAbility.size)
                .toString()
                .plus(pokemonDetails.hiddenAbility.joinToString(" and "))
        }

        viewModel.previousEvolutionDetails.observe(viewLifecycleOwner) { previousEvolutionDetails ->
            binding.evolvesFrom.visibility = View.VISIBLE
            binding.evolvesFromImage.load(previousEvolutionDetails.sprite)
            binding.evolvesFromName.text = previousEvolutionDetails.name
        }

        viewModel.nextEvolutionDetails.observe(viewLifecycleOwner) { nextEvolutionDetails ->
            binding.evolvesTo.visibility = View.VISIBLE
            binding.evolvesToImage.load(nextEvolutionDetails.sprite)
            binding.evolvesToName.text = nextEvolutionDetails.name
        }
    }

}
