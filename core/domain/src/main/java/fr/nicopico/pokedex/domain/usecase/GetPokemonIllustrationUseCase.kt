package fr.nicopico.pokedex.domain.usecase

import fr.nicopico.pokedex.domain.model.PokemonId
import java.net.URI

class GetPokemonIllustrationUseCase : UseCase<PokemonId, URI> {
    override suspend fun execute(parameter: PokemonId): URI {
        return URI.create("https://pokeres.bastionbot.org/images/pokemon/$parameter.png")
    }
}
