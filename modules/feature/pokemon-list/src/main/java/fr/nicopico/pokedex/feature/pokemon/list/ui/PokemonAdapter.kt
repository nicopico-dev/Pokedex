package fr.nicopico.pokedex.feature.pokemon.list.ui

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import fr.nicopico.pokedex.domain.model.Pokemon
import fr.nicopico.pokedex.feature.pokemon.list.R
import fr.nicopico.pokedex.feature.pokemon.list.databinding.PokemonListItemBinding

internal class PokemonAdapter(
    context: Context,
    private val onPokemonClicked: (Pokemon) -> Unit
) : ListAdapter<Pokemon, PokemonAdapter.ViewHolder>(DiffItemCallback()) {

    private val inflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = PokemonListItemBinding.inflate(inflater, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindTo(getItem(position))
    }

    private class DiffItemCallback : DiffUtil.ItemCallback<Pokemon>() {

        override fun areItemsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: Pokemon, newItem: Pokemon): Boolean {
            return oldItem == newItem
        }
    }

    inner class ViewHolder(
        private val binding: PokemonListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        private lateinit var pokemon: Pokemon

        init {
            binding.root.setOnClickListener { onPokemonClicked(pokemon) }
        }

        fun bindTo(item: Pokemon) {
            this.pokemon = item
            binding.txtPokemonName.text = item.name
            binding.imgPokemon.load(item.illustrationUrl) {
                placeholder(R.drawable.pokemon_list_item_placeholder)
                this.crossfade(true)
            }
        }
    }
}
