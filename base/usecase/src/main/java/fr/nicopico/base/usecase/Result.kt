package fr.nicopico.base.usecase

sealed class Result<T> {
    class Success<T>(val value: T) : Result<T>()
    class Failure<T>(val error: Throwable) : Result<T>()

    companion object {
        fun <T> success(value: T): Result<T> =
            Success(value)
        fun <T> failure(error: Throwable): Result<T> =
            Failure(error)

        @Suppress("TooGenericExceptionCaught")
        inline fun <T> runCatching(block: () -> T): Result<T> {
            return try {
                success(block())
            } catch (e: Throwable) {
                failure(e)
            }
        }
    }

    fun getOrNull(): T? = when (this) {
        is Success -> this.value
        else -> null
    }
}

inline fun <R, T : R> Result<T>.getOrElse(onFailure: (exception: Throwable) -> R): R = when (this) {
    is Result.Success -> this.value
    is Result.Failure -> onFailure(this.error)
}

fun <T> Result<T>.getOrThrow(): T = when (this) {
    is Result.Success -> this.value
    is Result.Failure -> throw this.error
}

fun <R, T : R> Result<T>.map(transform: (value: T) -> R): Result<R> = when (this) {
    is Result.Success -> Result.success(
        transform(this.value)
    )
    is Result.Failure -> Result.failure(
        this.error
    )
}

fun <R, T : R> Result<T>.recover(transform: (exception: Throwable) -> R): Result<R> = when (this) {
    is Result.Success -> Result.success(
        this.value
    )
    is Result.Failure -> Result.success(
        transform(this.error)
    )
}

inline fun <T> Result<T>.onSuccess(action: (value: T) -> Unit): Result<T> {
    if (this is Result.Success) {
        action(value)
    }
    return this
}

inline fun <T> Result<T>.onFailure(action: (exception: Throwable) -> Unit): Result<T> {
    if (this is Result.Failure) {
        action(error)
    }
    return this
}
