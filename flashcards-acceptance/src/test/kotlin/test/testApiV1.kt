package com.github.kondury.flashcards.blackbox.test

import io.kotest.core.spec.style.FunSpec
import com.github.kondury.flashcards.blackbox.fixture.client.Client
import com.github.kondury.flashcards.blackbox.test.action.v1.createReview

fun FunSpec.testApiV1(client: Client) {
    context("v1") {
        test("Create Review ok") {
            client.createReview()
        }
    }
}