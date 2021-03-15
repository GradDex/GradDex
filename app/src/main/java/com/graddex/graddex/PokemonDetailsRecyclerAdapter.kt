package com.graddex.graddex

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.graddex.graddex.models.*
import com.graddex.graddex.models.PokemonDetailsResponse

class PokemonDetailsRecyclerAdapter
    : ListAdapter<PokemonDetailsResponse, PokemonDetailsViewHolder>(
    object : DiffUtil.ItemCallback<PokemonDetailsResponse>() {
        override fun areItemsTheSame(
            oldItem: PokemonDetailsResponse,
            newItem: PokemonDetailsResponse
        ): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(
            oldItem: PokemonDetailsResponse,
            newItem: PokemonDetailsResponse
        ): Boolean {
            return oldItem == newItem
        }
    }
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonDetailsViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item_pokemon, parent, false)

        return PokemonDetailsViewHolder(view)
    }

    override fun onBindViewHolder(holder: PokemonDetailsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

}

class PokemonDetailsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val pokemonNameText: TextView = itemView.findViewById(R.id.pokemon_name)

    fun bind(item: PokemonDetailsResponse) {
        pokemonNameText.text = item.name
    }
}