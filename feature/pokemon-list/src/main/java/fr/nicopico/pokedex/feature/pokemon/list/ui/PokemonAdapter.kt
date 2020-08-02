package fr.nicopico.pokedex.feature.pokemon.list.ui

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import fr.nicopico.pokedex.domain.model.Pokemon

internal class PokemonAdapter(
    context: Context
) : ListAdapter<Pokemon, PokemonAdapter.ViewHolder>(DiffItemCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        TODO("onCreateViewHolder is not implemented")
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("onBindViewHolder is not implemented")
    }

    inner class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView)

    private class DiffItemCallback : DiffUtil.ItemCallback<Pokemon>() {
        override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
            TODO("areItemsTheSame is not implemented")
        }

        override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
            TODO("areContentsTheSame is not implemented")
        }

    }
}
