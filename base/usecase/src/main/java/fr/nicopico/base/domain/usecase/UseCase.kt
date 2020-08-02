package fr.nicopico.base.domain.usecase

interface UseCase<PARAMETER, RESULT> {
    suspend fun execute(parameter: PARAMETER): Result<RESULT>
}

abstract class NoParamUseCase<RESULT> :
    UseCase<Unit, RESULT> {
    final override suspend fun execute(parameter: Unit): Result<RESULT> = execute()

    abstract suspend fun execute(): Result<RESULT>
}
