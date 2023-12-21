package com.github.kondury.flashcards.placedcards.biz.permissions

import com.github.kondury.flashcards.placedcards.biz.common.initProcessor
import com.github.kondury.flashcards.placedcards.biz.common.initSingleMockRepository
import com.github.kondury.flashcards.placedcards.biz.common.setPrincipal
import com.github.kondury.flashcards.placedcards.common.models.PlacedCardCommand.CREATE_PLACED_CARD
import com.github.kondury.flashcards.placedcards.common.models.PlacedCardId
import com.github.kondury.flashcards.placedcards.common.models.UserId
import com.github.kondury.flashcards.placedcards.common.permissions.FcUserGroups.ADMIN
import com.github.kondury.flashcards.placedcards.common.permissions.FcUserGroups.USER
import com.github.kondury.flashcards.placedcards.stubs.PlacedCardStub
import kotlin.test.Test


class PermissionsCreatePlacedCardTest {
    private val stub = PlacedCardStub.get()
    private val actorOwner = stub.ownerId
    private val anyOwner = UserId("any-user-id-1")
    private val anyActor = UserId("actor-id-1")
    private val newUuid = "10000000-0000-0000-0000-000000000001"

    private val processor by lazy {
        val repository = initSingleMockRepository(newUuid = newUuid)
        initProcessor(repository)
    }

    @Test
    fun `A User group user is allowed to create a placed card owned by oneself`() =
        runPermissionSuccessTest(processor, CREATE_PLACED_CARD) {
            requestPlacedCard = stub.copy(id = PlacedCardId.NONE, ownerId = actorOwner)
            setPrincipal(actorOwner, setOf(USER))
        }

    @Test
    fun `A User group user is not allowed to create a placed card owned by another user`() =
        runPermissionFailTest(processor, CREATE_PLACED_CARD) {
            requestPlacedCard = stub.copy(id = PlacedCardId.NONE, ownerId = anyOwner)
            setPrincipal(anyActor, setOf(USER))
        }


    @Test
    fun `An Admin group user is allowed to create a placed card owned by any user`() =
        runPermissionSuccessTest(processor, CREATE_PLACED_CARD) {
            requestPlacedCard = stub.copy(id = PlacedCardId.NONE, ownerId = anyOwner)
            setPrincipal(anyActor, setOf(ADMIN))
        }
}


