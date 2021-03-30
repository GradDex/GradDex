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
import java.util.*

private const val pokemonKey: String = "pokemonKey"

class PokemonDetailsFragment(args: Bundle) : Fragment() {

    constructor(id: String) : this(bundleOf(pokemonKey to id))

    private val pokemonId: String = args.getString(pokemonKey, "")

    private lateinit var binding: FragmentPokemonDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        Log.d(tag, "PokemonID: $pokemonId")

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
        viewModel.syncPokemonDetails(pokemonId)

        // Update UI with details
        viewModel.pokemonSpriteFront.observe(viewLifecycleOwner,
            { pokemonSpriteFront ->
                binding.pokemonFrontImage.load(pokemonSpriteFront)
                binding.statusText.visibility = View.GONE
            })

        viewModel.pokemonSpriteBack.observe(viewLifecycleOwner) { pokemonSpriteBack ->
            binding.pokemonBackImage.load(pokemonSpriteBack)
        }

        viewModel.pokemonName.observe(viewLifecycleOwner) { pokemonName ->
            binding.pokemonName.text = pokemonName.capitalize(Locale.ROOT)

            viewModel.secondEvolution.observe(viewLifecycleOwner) { secondEvolution ->
                if (secondEvolution == pokemonName) {
                    viewModel.thirdEvolution.observe(viewLifecycleOwner) { thirdEvolution ->
                        if (thirdEvolution != null) {
                            binding.evolvesTo.visibility = View.VISIBLE
                            binding.evolvesToName.text = thirdEvolution.capitalize(Locale.ROOT)
                            viewModel.thirdEvolutionUrl.observe(viewLifecycleOwner) { thirdEvolutionUrl ->

                            }
                        }
                    }
                } else {
                    viewModel.secondEvolutionUrl.observe(viewLifecycleOwner) { secondEvolutionUrl ->
                        binding.evolvesTo.visibility = View.VISIBLE
                        binding.evolvesToName.text = secondEvolution.capitalize(Locale.ROOT)
                        viewModel.getEvolutionSprites(secondEvolutionUrl)
                    }
                }
            }
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

        viewModel.thirdEvolutionUrl.observe(viewLifecycleOwner) { thirdEvolutionUrl ->
            viewModel.getEvolutionSprites(thirdEvolutionUrl!!)
        }

        viewModel.secondEvolutionUrl.observe(viewLifecycleOwner) { secondEvolutionUrl ->
            viewModel.getEvolutionSprites(secondEvolutionUrl)
        }

        viewModel.evolutionSprite.observe(viewLifecycleOwner) { evolutionSprite ->
            binding.evolvesToImage.load(evolutionSprite)
        }

    }

}