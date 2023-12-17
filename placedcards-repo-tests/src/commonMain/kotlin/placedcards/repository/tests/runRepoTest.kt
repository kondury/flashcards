package com.github.kondury.flashcards.placedcards.repository.tests

import com.github.kondury.flashcards.placedcards.common.repository.PlacedCardDbResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import kotlin.reflect.KSuspendFunction1
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.test.fail

internal fun <T> runRepoSuccessTest(
    request: T,
    repoOperation: KSuspendFunction1<T, PlacedCardDbResponse>,
    assertSpecific: (PlacedCardDbResponse) -> Unit
) = runRepoTest {
    val dbResponse = repoOperation(request)
    assertEquals(true, dbResponse.isSuccess)
    assertTrue(dbResponse.errors.isEmpty(), "Errors list is expected to be empty")
    assertSpecific(dbResponse)
}

internal fun <T> runRepoNotFoundErrorTest(request: T, repoOperation: KSuspendFunction1<T, PlacedCardDbResponse>) =
    runRepoErrorTest(request, repoOperation) { dbResponse ->
        assertEquals(null, dbResponse.data)
        val error = dbResponse.errors.find { it.code == "not-found" }
            ?: fail("Errors list is expected to contain not-found error")
        assertEquals("id", error.field)
    }

internal fun <T> runRepoConcurrencyErrorTest(request: T, repoOperation: KSuspendFunction1<T, PlacedCardDbResponse>) =
    runRepoErrorTest(request, repoOperation) { dbResponse ->
        assertNotNull(
            dbResponse.data,
            "Data is expected to contain a card instance from repository having the 'bad' lock"
        )
        val error = dbResponse.errors.find { it.code == "concurrency" }
            ?: fail("Errors list is expected to contain concurrency error")
        assertEquals("lock", error.field)
    }

internal fun <T> runRepoErrorTest(
    request: T,
    repoOperation: KSuspendFunction1<T, PlacedCardDbResponse>,
    assertSpecific: (PlacedCardDbResponse) -> Unit
) = runRepoTest {
    val dbResponse = repoOperation(request)
    assertEquals(false, dbResponse.isSuccess)
    assertSpecific(dbResponse)
}

internal fun runRepoTest(testBody: suspend TestScope.() -> Unit) = runTest {
    withContext(Dispatchers.Default) {
        testBody()
    }
}
