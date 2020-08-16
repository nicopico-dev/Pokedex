package fr.nicopico.pokedex.feature.pokemon.list.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import fr.nicopico.base.android.dependency.injection.injectedViewModels
import fr.nicopico.pokedex.feature.pokemon.list.R
import fr.nicopico.pokedex.feature.pokemon.list.databinding.PokemonListFragmentBinding
import fr.nicopico.pokedex.resources.recyclerview.SpacingItemDecoration

class PokemonListFragment : Fragment() {

    private lateinit var binding: PokemonListFragmentBinding

    private val viewModel: PokemonListViewModel by injectedViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PokemonListFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val columnCount = resources.getInteger(R.integer.pokemon_list_column_count)
        val itemSpacing = resources.getDimensionPixelSize(R.dimen.gutter)

        with(binding.rcvPokemons) {
            layoutManager = GridLayoutManager(context, columnCount)
            adapter = PokemonAdapter(context).apply {
                viewModel.pokemons.observe(viewLifecycleOwner, Observer { submitList(it) })
            }
            addItemDecoration(SpacingItemDecoration(layoutManager!!, itemSpacing))
        }
    }
}
