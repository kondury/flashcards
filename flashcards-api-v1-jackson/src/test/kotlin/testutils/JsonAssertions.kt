package com.github.kondury.flashcards.api.v1.testutils

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.assertAll

fun assertJson(
    type: String,
    json: String,
    assertions: List<() -> Unit>
) {
    assertAll(
        """
                Serialized Json should contain several key:value pairs:
                type="$type",
                json=$json
                """.trimIndent(),
        assertions
    )
}

fun generateJsonAssertions(jsonSubstrings: List<String>, json: String) =
    jsonSubstrings.map { expected ->
        {
            Assertions.assertTrue(json.contains(expected)) { "Serialized json does not contain expected: $expected" }
        }
    }