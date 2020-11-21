package fr.nicopico.base.tests

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class CoroutineTestRule(
    val scope: TestCoroutineScope = TestCoroutineScope(),
    private val testDispatcher: TestCoroutineDispatcher = TestCoroutineDispatcher()
) : TestWatcher() {

    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
        try {
            scope.cleanupTestCoroutines()
        } catch (ignored: Exception) {
            // no-op
        }
        super.finished(description)
    }

    fun runBlockingTest(block: suspend TestCoroutineScope.() -> Unit) =
        scope.runBlockingTest(block)
}
