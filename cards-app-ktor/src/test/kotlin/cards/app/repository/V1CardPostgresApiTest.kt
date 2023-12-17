package com.github.kondury.flashcards.cards.app.repository

import com.github.kondury.flashcards.cards.api.v1.models.DebugResource
import com.github.kondury.flashcards.cards.api.v1.models.RunMode
import com.github.kondury.flashcards.cards.app.V1CardApiContract
import com.github.kondury.flashcards.cards.app.repository.SqlTestCompanion.repoUnderTestContainer
import com.github.kondury.flashcards.cards.app.V1CardApiContract.Companion.NEW_UUID
import com.github.kondury.flashcards.cards.app.V1CardApiContract.Companion.stubCard
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll


class V1CardPostgresApiTest : V1CardApiContract {

    override fun getRepository(test: String) =
        repoUnderTestContainer(test = test, initObjects = listOf(stubCard), randomUuid = { NEW_UUID })
    override val assertSpecificOn: Boolean = true
    override val debugResource: DebugResource = DebugResource(mode = RunMode.TEST)

    companion object {
        @BeforeAll
        @JvmStatic
        fun tearUp() {
            SqlTestCompanion.start()
        }

        @AfterAll
        @JvmStatic
        fun tearDown() {
            SqlTestCompanion.stop()
        }
    }
}
