package com.graddex.graddex

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.graddex.graddex.models.PokemonResponse_Result

class PokemonRecyclerAdapter(val listener: (String) -> Unit)
    : ListAdapter<PokemonResponse_Result, PokemonRecyclerAdapter.PokemonViewHolder>(
        object : DiffUtil.ItemCallback<PokemonResponse_Result>() {
            override fun areItemsTheSame(
                    oldItem: PokemonResponse_Result,
                    newItem: PokemonResponse_Result
            ): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                    oldItem: PokemonResponse_Result,
                    newItem: PokemonResponse_Result
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

        fun bind(item: PokemonResponse_Result) {
            pokemonNameText.text = item.name
            pokemonUrlNameText.text = item.url
            itemView.setOnClickListener {
                Log.d("PokemonID", "Pokemon ID: ${item.url}")
                listener(item.url)
            }


        }
    }
}

