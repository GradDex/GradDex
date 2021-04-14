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
//        viewModel.syncPokemonDetails(pokemonId)

        // Update UI with details
        /**viewModel.pokemonSpriteFront.observe(viewLifecycleOwner,
        { pokemonSpriteFront ->
        binding.pokemonFrontImage.load(pokemonSpriteFront)
        binding.statusText.visibility = View.GONE
        })

        viewModel.pokemonSpriteBack.observe(viewLifecycleOwner) { pokemonSpriteBack ->
        binding.pokemonBackImage.load(pokemonSpriteBack)
        }

        viewModel.pokemonName.observe(viewLifecycleOwner) { pokemonName ->
        binding.pokemonName.text = pokemonName.capitalize(Locale.ROOT)
        }

        viewModel.pokemonTypes.observe(viewLifecycleOwner) { pokemonTypes ->
        val pokemonTypeList = mutableListOf<String>()
        for (element in pokemonTypes) {
        pokemonTypeList += element.type.name.capitalize(Locale.ROOT)
        }
        binding.pokemonTypes.text = pokemonTypeList.joinToString(" | ")
        }

        viewModel.pokemonAbilities.observe(viewLifecycleOwner) { pokemonAbilities ->
        val pokemonAbilitiesList = mutableListOf<String>()
        for (element in pokemonAbilities) {
        if (!element.is_hidden) {
        pokemonAbilitiesList += element.ability.name.capitalize(Locale.ROOT)
        } else {
        val pokemonHiddenAbility = element.ability.name.capitalize(Locale.ROOT)
        val hiddenAbilityText =
        String.format("%s %s", getString(hidden_ability), pokemonHiddenAbility)
        binding.pokemonHiddenAbility.text = hiddenAbilityText
        }
        }
        if (pokemonAbilitiesList.size > 1) {
        val pokemonAbilityText = String.format(
        "%s %s",
        getString(abilities),
        pokemonAbilitiesList.joinToString(" and ")
        )
        binding.pokemonAbilities.text = pokemonAbilityText
        } else {
        val pokemonAbilityText =
        String.format("%s %s", getString(ability), pokemonAbilitiesList[0])
        binding.pokemonAbilities.text = pokemonAbilityText
        }
        }

        viewModel.previousEvolution.observe(viewLifecycleOwner) { previousEvolution ->
        binding.evolvesFrom.visibility = View.VISIBLE
        binding.evolvesFromName.text = previousEvolution.capitalize(Locale.ROOT)
        }

        viewModel.previousEvolutionSprite.observe(viewLifecycleOwner) { previousEvolutionSprite ->
        binding.evolvesFromImage.load(previousEvolutionSprite)
        }

        viewModel.nextEvolution.observe(viewLifecycleOwner) { nextEvolution ->
        binding.evolvesTo.visibility = View.VISIBLE
        binding.evolvesToName.text = nextEvolution.capitalize(Locale.ROOT)
        }

        viewModel.nextEvolutionSprite.observe(viewLifecycleOwner) { nextEvolutionSprite ->
        binding.evolvesToImage.load(nextEvolutionSprite)
        }**/

        viewModel.pokemonDetails.observe(viewLifecycleOwner) {pokemonDetails ->
            binding.statusText.visibility = View.GONE
            binding.pokemonFrontImage.load(pokemonDetails.frontSprite)
            binding.pokemonBackImage.load(pokemonDetails.backSprite)
            binding.pokemonName.text = pokemonDetails.name
            binding.pokemonAbilities.text = pokemonDetails.abilities.joinToString { " and " }
            binding.pokemonHiddenAbility.text = pokemonDetails.hiddenAbility
        }
    }


}
