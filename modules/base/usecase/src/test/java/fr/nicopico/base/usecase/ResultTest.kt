package fr.nicopico.base.usecase

import com.google.common.truth.Truth.assertThat
import io.mockk.*
import org.junit.Test
import java.lang.RuntimeException
import java.lang.UnsupportedOperationException
import java.util.NoSuchElementException
import kotlin.IllegalStateException

class ResultTest {

    @Test
    fun `runCatching returns a Success on success`() {
        // When
        val result = Result.runCatching { 22 }

        // Then
        assertThat(result).isInstanceOf(Result.Success::class.java)
        assertThat((result as Result.Success).value).isEqualTo(22)
    }

    @Test
    fun `runCatching returns a Success on error`() {
        // Given
        val thrownException = UnsupportedOperationException("not implemented")

        // When
        val result = Result.runCatching {
            throw thrownException
        }

        // Then
        assertThat(result).isInstanceOf(Result.Failure::class.java)
        assertThat((result as Result.Failure).error).isEqualTo(thrownException)
    }

    @Test
    fun `getOrNull return value on success`() {
        // Given
        val result = Result.success(22)

        // When
        val actual = result.getOrNull()

        // Then
        assertThat(actual).isEqualTo(22)
    }

    @Test
    fun `getOrNull return null on failure`() {
        // Given
        val result = Result.failure<Int>(UnsupportedOperationException())

        // When
        val actual = result.getOrNull()

        // Then
        assertThat(actual).isNull()
    }

    @Test
    fun `getOrElse return value on success`() {
        // Given
        val result = Result.success(22)

        // When
        val actual = result.getOrElse { 42 }

        // Then
        assertThat(actual).isEqualTo(22)
    }

    @Test
    fun `getOrElse return 'else' on failure`() {
        // Given
        val result = Result.failure<Int>(RuntimeException())

        // When
        val actual = result.getOrElse { 42 }

        // Then
        assertThat(actual).isEqualTo(42)
    }

    @Test
    fun `getOrThrow return value on success`() {
        // Given
        val result = Result.success(22)

        // When
        val actual = result.getOrThrow()

        // Then
        assertThat(actual).isEqualTo(22)
    }

    @Test
    fun `getOrThrow throw the error on failure`() {
        // Given
        val error = NoSuchElementException()
        val result = Result.failure<Int>(error)

        // When
        var actual: Throwable? = null
        try {
            result.getOrThrow()
        } catch (e: Throwable) {
            actual = e
        }

        // Then
        assertThat(actual).isSameInstanceAs(error)
    }

    @Test
    fun `map transform the value on success`() {
        // Given
        val result = Result.success(22)

        // When
        val actual = result.map { it * 2 }

        // Then
        assertThat(actual).isInstanceOf(Result.Success::class.java)
        val success = actual as Result.Success<Int>
        assertThat(success.value).isEqualTo(44)
    }

    @Test
    fun `map pass the error on failure`() {
        // Given
        val error = NotImplementedError()
        val result = Result.failure<Int>(error)

        // When
        val actual = result.map { it * 2 }

        // Then
        assertThat(actual).isInstanceOf(Result.Failure::class.java)
        val failure = actual as Result.Failure
        assertThat(failure.error).isEqualTo(error)
    }

    @Test
    fun `recover does nothing on success`() {
        // Given
        val result = Result.success(22)

        // When
        val actual = result.recover { 123 }

        // Then
        assertThat(actual).isInstanceOf(Result.Success::class.java)
        val success = actual as Result.Success<Int>
        assertThat(success.value).isEqualTo(22)
    }

    @Test
    fun `recover transform the error on failure`() {
        // Given
        val result = Result.failure<Int>(IllegalStateException())

        // When
        val actual = result.recover { 123 }

        // Then
        assertThat(actual).isInstanceOf(Result.Success::class.java)
        val success = actual as Result.Success<Int>
        assertThat(success.value).isEqualTo(123)
    }

    @Test
    fun `onSuccess action is run on success`() {
        // Given
        val result = Result.success(22)
        val action: (Int) -> Unit = mockk()
        every { action(any()) } returns Unit

        // When
        val actual = result.onSuccess(action)

        // Then
        assertThat(actual).isSameInstanceAs(result)

        verify { action(22) }
        confirmVerified(action)
    }

    @Test
    fun `onSuccess action is not run on failure`() {
        // Given
        val result = Result.failure<Int>(IllegalStateException())
        val action: (Int) -> Unit = mockk()
        every { action(any()) } returns Unit

        // When
        val actual = result.onSuccess(action)

        // Then
        assertThat(actual).isSameInstanceAs(result)

        verify { action(any()) wasNot Called }
        confirmVerified(action)
    }

    @Test
    fun `onFailure action is not run on success`() {
        // Given
        val result = Result.success(22)
        val action: (Throwable) -> Unit = mockk()
        every { action(any()) } returns Unit

        // When
        val actual = result.onFailure(action)

        // Then
        assertThat(actual).isSameInstanceAs(result)

        verify { action(any()) wasNot Called}
        confirmVerified(action)
    }

    @Test
    fun `onFailure action is run on failure`() {
        // Given
        val error = IllegalStateException()
        val result = Result.failure<Int>(error)
        val action: (Throwable) -> Unit = mockk()
        every { action(any()) } returns Unit

        // When
        val actual = result.onFailure(action)

        // Then
        assertThat(actual).isSameInstanceAs(result)

        verify { action(error) }
        confirmVerified(action)
    }
}
