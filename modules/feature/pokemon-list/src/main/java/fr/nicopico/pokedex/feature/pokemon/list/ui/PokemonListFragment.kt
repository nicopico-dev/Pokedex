package fr.nicopico.pokedex.feature.pokemon.list.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import fr.nicopico.pokedex.feature.pokemon.list.R
import fr.nicopico.pokedex.feature.pokemon.list.databinding.PokemonListFragmentBinding
import fr.nicopico.pokedex.resources.recyclerview.SpacingItemDecoration
import org.koin.androidx.viewmodel.ext.android.viewModel

class PokemonListFragment : Fragment() {

    private lateinit var binding: PokemonListFragmentBinding

    private val viewModel: PokemonListViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = PokemonListFragmentBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val columnCount = resources.getInteger(R.integer.pokemon_list_column_count)
        val itemSpacing = resources.getDimensionPixelSize(R.dimen.gutter)

        with(binding.rcvPokemons) {
            layoutManager = GridLayoutManager(context, columnCount)
            adapter = PokemonAdapter(context, viewModel::onPokemonClicked).apply {
                viewModel.pokemons.observe(viewLifecycleOwner, { submitList(it) })
            }
            addItemDecoration(SpacingItemDecoration(layoutManager!!, itemSpacing))
        }
    }
}
