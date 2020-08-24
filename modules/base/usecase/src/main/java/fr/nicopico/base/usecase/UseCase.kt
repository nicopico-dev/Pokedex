package fr.nicopico.base.usecase

interface UseCase<PARAMETER, RESULT> {
    suspend fun execute(parameter: PARAMETER): RESULT
}
