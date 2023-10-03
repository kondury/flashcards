package com.github.kondury.flashcards.cards.api.v1.testutils

import com.github.kondury.flashcards.cards.api.v1.models.DebugResource
import com.github.kondury.flashcards.cards.api.v1.models.DebugStub
import com.github.kondury.flashcards.cards.api.v1.models.Error
import com.github.kondury.flashcards.cards.api.v1.models.RunMode

val debug = DebugResource(
    mode = RunMode.TEST,
    stub = DebugStub.SUCCESS
)

val error = Error(
    code = "errorCode",
    group = "errorGroup",
    field = "errorField",
    message = "errorMessage",
)

val errorJsonChecks = listOf(
    "\"code\":\"errorCode\"",
    "\"group\":\"errorGroup\"",
    "\"field\":\"errorField\"",
    "\"message\":\"errorMessage\"",
)