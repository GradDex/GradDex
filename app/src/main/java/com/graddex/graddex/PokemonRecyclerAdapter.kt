package com.graddex.graddex

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.graddex.graddex.models.PokemonResponseResult

class PokemonRecyclerAdapter(val listener: (name: String, url: String) -> Unit)
    : ListAdapter<PokemonResponseResult, PokemonRecyclerAdapter.PokemonViewHolder>(
        object : DiffUtil.ItemCallback<PokemonResponseResult>() {
            override fun areItemsTheSame(
                    oldItem: PokemonResponseResult,
                    newItem: PokemonResponseResult
            ): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                    oldItem: PokemonResponseResult,
                    newItem: PokemonResponseResult
            ): Boolean {
                return oldItem == newItem
            }
        }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonViewHolder {
        val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.list_item_pokemon, parent, false)

        return PokemonViewHolder(view)
    }

    override fun onBindViewHolder(holder: PokemonViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PokemonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val pokemonNameText: TextView = itemView.findViewById(R.id.pokemon_name)
        private val pokemonUrlNameText: TextView = itemView.findViewById(R.id.pokemon_url)

        fun bind(item: PokemonResponseResult) {
            pokemonNameText.text = item.name
            pokemonUrlNameText.text = item.url
            itemView.setOnClickListener {
                Log.d("PokemonID", "Pokemon ID: ${item.url}")
                listener(item.name, item.url)
            }
        }
    }
}

