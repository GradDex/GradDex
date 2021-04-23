package com.graddex.graddex

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.graddex.graddex.databinding.FragmentPokemonListBinding
import com.graddex.graddex.models.PokemonDetailsViewModel
import com.graddex.graddex.models.PokemonListViewModel
import java.util.zip.Inflater

class PokemonListFragment : Fragment() {

    private lateinit var binding: FragmentPokemonListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_pokemon_list, container, false
        )

        return binding.root
    }

    private lateinit var pokemonRecyclerView: RecyclerView
    private lateinit var pokemonAdapter: PokemonRecyclerAdapter

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // Call the Pokemon List view model
        val viewModel: PokemonListViewModel = ViewModelProvider(this)
            .get(PokemonListViewModel::class.java)

        binding.statusText.text = getString(R.string.loading)
        viewModel.getPokemonList()

        // Update UI with list of Pokemon
        viewModel.pokemonList.observe(viewLifecycleOwner) { pokemonList ->
            pokemonAdapter = PokemonRecyclerAdapter { name: String, url: String ->
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.add(R.id.pokemon_details_fragment, PokemonDetailsFragment(name, url))
                    .addToBackStack(null)
                    .commit()
            }
            pokemonRecyclerView = binding.pokemonRecyclerview
            pokemonRecyclerView.layoutManager = LinearLayoutManager(activity)
            pokemonRecyclerView.adapter = this.pokemonAdapter
            binding.statusText.visibility = View.GONE
            pokemonRecyclerView.visibility = View.VISIBLE
            this.pokemonAdapter.submitList(pokemonList.results)
        }

    }

}