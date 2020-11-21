package fr.nicopico.base.tests

import kotlinx.coroutines.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.FileNotFoundException

@ExperimentalCoroutinesApi
class CoroutineTestRuleTest {

    @get:Rule
    val coroutineTestRule = CoroutineTestRule()

    private lateinit var foo: Foo

    @Before
    fun setUp() {
        foo = Foo(coroutineTestRule.scope)
    }

    @Test
    fun testOk() = coroutineTestRule.runBlockingTest {
        foo.doSomething()
    }

    @Test(expected = FileNotFoundException::class)
    fun testThrow() = coroutineTestRule.runBlockingTest {
        foo.throwSomething()
    }

    @Test(expected = FileNotFoundException::class)
    fun testThrowAsync() = coroutineTestRule.runBlockingTest {
        foo.throwSomethingAsync()
    }
}

private class Foo(private val scope: CoroutineScope) {
    fun doSomething() {
        scope.launch {
            delay(2000)
        }
    }

    fun throwSomething() {
        scope.launch {
            throw FileNotFoundException("Something bad happened")
        }
    }

    suspend fun throwSomethingAsync() {
        val job1 = scope.async { throw FileNotFoundException("Something bad happened") }
        val job2 = scope.async { delay(2000) }
        awaitAll(job1, job2)
    }
}
