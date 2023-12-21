package com.github.kondury.flashcards.placedcards.biz.permissions

import com.github.kondury.flashcards.placedcards.biz.common.initProcessor
import com.github.kondury.flashcards.placedcards.biz.common.initSingleMockRepository
import com.github.kondury.flashcards.placedcards.biz.common.setPrincipal
import com.github.kondury.flashcards.placedcards.common.models.FcSearchStrategy
import com.github.kondury.flashcards.placedcards.common.models.PlacedCardCommand.SELECT_PLACED_CARD
import com.github.kondury.flashcards.placedcards.common.models.UserId
import com.github.kondury.flashcards.placedcards.common.permissions.FcUserGroups.ADMIN
import com.github.kondury.flashcards.placedcards.common.permissions.FcUserGroups.USER
import com.github.kondury.flashcards.placedcards.stubs.PlacedCardStub
import kotlin.test.Test


class PermissionsSelectPlacedCardTest {
    private val stub = PlacedCardStub.get()
    private val actorOwner = stub.ownerId
    private val anyOwner = stub.ownerId
    private val anyActor = UserId("actor-id-1")
    private val newUuid = "10000000-0000-0000-0000-000000000001"


    private val processor by lazy {
        val repository = initSingleMockRepository(stub, newUuid)
        initProcessor(repository)
    }

    @Test
    fun `A User group user is allowed to select a placed card owned by oneself`() =
        runPermissionSuccessTest(processor, SELECT_PLACED_CARD) {
            requestOwnerId = actorOwner
            requestWorkBox = stub.box
            requestSearchStrategy = FcSearchStrategy.EARLIEST_CREATED
            setPrincipal(actorOwner, setOf(USER))
        }

    @Test
    fun `A User group user is not allowed to select a placed card owned by another user`() =
        runPermissionFailTest(processor, SELECT_PLACED_CARD) {
            requestOwnerId = anyOwner
            requestWorkBox = stub.box
            requestSearchStrategy = FcSearchStrategy.EARLIEST_CREATED
            setPrincipal(anyActor, setOf(USER))
        }

    @Test
    fun `An Admin group user is allowed to select a placed card owned by any user`() =
        runPermissionSuccessTest(processor, SELECT_PLACED_CARD) {
            requestOwnerId = anyOwner
            requestWorkBox = stub.box
            requestSearchStrategy = FcSearchStrategy.EARLIEST_CREATED
            setPrincipal(anyActor, setOf(ADMIN))
        }
}


