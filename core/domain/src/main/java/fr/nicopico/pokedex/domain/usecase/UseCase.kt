package fr.nicopico.pokedex.domain.usecase

interface UseCase<PARAMETER, RESULT> {
    suspend fun execute(parameter: PARAMETER): RESULT
}

abstract class NoParamUseCase<RESULT> : UseCase<Unit, RESULT> {
    final override suspend fun execute(parameter: Unit): RESULT = execute()

    abstract suspend fun execute(): RESULT
}
