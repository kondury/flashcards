package com.github.kondury.flashcards.placedcards.biz.permissions

import com.github.kondury.flashcards.placedcards.biz.common.initProcessor
import com.github.kondury.flashcards.placedcards.biz.common.initSingleMockRepository
import com.github.kondury.flashcards.placedcards.biz.common.setPrincipal
import com.github.kondury.flashcards.placedcards.common.models.PlacedCardCommand.DELETE_PLACED_CARD
import com.github.kondury.flashcards.placedcards.common.models.UserId
import com.github.kondury.flashcards.placedcards.common.permissions.FcUserGroups.ADMIN
import com.github.kondury.flashcards.placedcards.common.permissions.FcUserGroups.USER
import com.github.kondury.flashcards.placedcards.stubs.PlacedCardStub
import kotlin.test.Test


class PermissionsDeletePlacedCardTest {
    private val stub = PlacedCardStub.get()
    private val actorOwner = stub.ownerId
    private val anyOwner = UserId("any-user-id-1")
    private val anyActor = UserId("actor-id-1")
    private val newUuid = "10000000-0000-0000-0000-000000000001"


    private val processor by lazy {
        val repository = initSingleMockRepository(stub, newUuid)
        initProcessor(repository)
    }

    @Test
    fun `A User group user is allowed to delete a placed card owned by oneself`() =
        runPermissionSuccessTest(processor, DELETE_PLACED_CARD) {
            requestPlacedCard = stub.copy(ownerId = actorOwner)
            setPrincipal(actorOwner, setOf(USER))
        }

    @Test
    fun `A User group user is not allowed to delete a placed card owned by another user`() =
        runPermissionFailTest(processor, DELETE_PLACED_CARD) {
            requestPlacedCard = stub.copy(ownerId = anyOwner)
            setPrincipal(anyActor, setOf(USER))
        }

    @Test
    fun `An Admin group user is allowed to delete placed card owned by any user`() =
        runPermissionSuccessTest(processor, DELETE_PLACED_CARD) {
            requestPlacedCard = stub.copy(ownerId = anyOwner)
            setPrincipal(anyActor, setOf(ADMIN))
        }
}


