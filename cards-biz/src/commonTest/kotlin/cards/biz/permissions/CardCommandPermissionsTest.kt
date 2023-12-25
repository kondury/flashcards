package com.github.kondury.flashcards.cards.biz.permissions

import com.github.kondury.flashcards.cards.biz.common.initProcessor
import com.github.kondury.flashcards.cards.biz.common.initSingleMockRepository
import com.github.kondury.flashcards.cards.biz.common.setPrincipal
import com.github.kondury.flashcards.cards.common.CardContext
import com.github.kondury.flashcards.cards.common.models.*
import com.github.kondury.flashcards.cards.common.models.CardCommand.*
import com.github.kondury.flashcards.cards.common.permissions.FcUserGroups
import com.github.kondury.flashcards.cards.common.permissions.FcUserGroups.ADMIN
import com.github.kondury.flashcards.cards.common.permissions.FcUserGroups.USER
import com.github.kondury.flashcards.cards.stubs.CardStub
import kotlinx.coroutines.test.runTest
import kotlin.test.*


class CardCommandPermissionsTest {

    private val newUuid = "10000000-0000-0000-0000-000000000001"
    private val initCard = CardStub.get()

    private val processor by lazy {
        val repository = initSingleMockRepository(initCard, newUuid)
        initProcessor(repository)
    }

    @Test
    fun `An Admin grou user is allowed to create a card`() =
        runPermissionSuccessTest(CREATE_CARD, initCard.copy(id = CardId.NONE), ADMIN)

    @Test
    fun `An User group user is not allowed to create a card`() =
        runPermissionFailTest(CREATE_CARD, initCard.copy(id = CardId.NONE), USER)

    @Test
    fun `An Admin group user is allowed to read a card`() =
        runPermissionSuccessTest(READ_CARD, initCard, ADMIN)

    @Test
    fun `A User group user is allowed to read a card`() =
        runPermissionSuccessTest(READ_CARD, initCard, USER)

    @Test
    fun `An Admin group user is allowed to delete a card`() =
        runPermissionSuccessTest(DELETE_CARD, initCard, ADMIN)

    @Test
    fun `A User group user is not allowed to delete a card`() =
        runPermissionFailTest(DELETE_CARD, initCard, USER)

    private fun runPermissionSuccessTest(command: CardCommand, card: Card, group: FcUserGroups) =
        runPermissionTest(command, card, group) { context ->
            assertEquals(FcState.FINISHING, context.state)
            assertTrue(context.isPermitted, "context.isPermitted is expected to be true")
            assertTrue { context.errors.isEmpty() }
            assertNotNull(context.responseCard)
        }

    private fun runPermissionFailTest(command: CardCommand, card: Card, group: FcUserGroups) =
        runPermissionTest(command, card, group) { context ->
            assertEquals(FcState.FAILING, context.state)
            assertEquals(1, context.errors.size)
            assertFalse(context.isPermitted, "context.isPermitted is expected to be false")
            assertTrue { "is not allowed to perform" in context.errors.first().message }
            assertEquals(Card.EMPTY, context.responseCard)
        }

    private fun runPermissionTest(
        command: CardCommand,
        card: Card,
        group: FcUserGroups,
        assertions: (CardContext) -> Unit
    ) = runTest {
        val context = CardContext(
            command = command,
            state = FcState.NONE,
            workMode = FcWorkMode.TEST,
            requestCard = card,
        )
        context.setPrincipal(groups = setOf(group))
        processor.exec(context)
        assertions(context)
    }
}


