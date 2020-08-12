package fr.nicopico.pokedex.feature.pokemon.list.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import fr.nicopico.base.android.dependency.injection.injectedViewModels
import fr.nicopico.pokedex.feature.pokemon.list.databinding.PokemonListFragmentBinding

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
        with(binding.rcvPokemons) {
            layoutManager = LinearLayoutManager(context)
            adapter = PokemonAdapter(context).apply {
                viewModel.pokemons.observe(viewLifecycleOwner, Observer { submitList(it) })
            }
        }
    }
}
