package com.github.kondury.flashcards.placedcards.repository.inmemory

import com.benasher44.uuid.uuid4
import com.github.kondury.flashcards.placedcards.common.models.*
import com.github.kondury.flashcards.placedcards.common.models.FcSearchStrategy.*
import com.github.kondury.flashcards.placedcards.common.repository.*
import com.github.kondury.flashcards.placedcards.repository.inmemory.model.PlacedCardEntity
import com.github.kondury.flashcards.placedcards.repository.inmemory.model.asStringOrNull
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class InMemoryPlacedCardRepository(
    initObjects: Collection<PlacedCard> = emptyList(),
    ttl: Duration = 2.minutes,
    val randomUuid: () -> String = { uuid4().toString() },
) : PlacedCardRepository {

    private val cache = Cache.Builder<String, PlacedCardEntity>()
        .expireAfterWrite(ttl)
        .build()
    private val mutex: Mutex = Mutex()

    init {
        initObjects.forEach {
//            println("SAVE $it")
            save(it)
        }
    }

    override suspend fun create(request: PlacedCardDbRequest): PlacedCardDbResponse {
        val key = randomUuid()
        val creationTime = Clock.System.now()
        val placedCard = request.placedCard.copy(
            id = PlacedCardId(key),
            lock = FcPlacedCardLock(randomUuid()),
            createdAt = creationTime,
            updatedAt = creationTime,
        )
        val entity = PlacedCardEntity(placedCard)
        cache.put(key, entity)
        return PlacedCardDbResponse.success(placedCard)
    }

    override suspend fun read(request: PlacedCardIdDbRequest): PlacedCardDbResponse {
        val key = request.id.asStringOrNull() ?: return PlacedCardDbResponse.emptyIdErrorResponse
        return cache.get(key)
            ?.toInternal()
            ?.let { PlacedCardDbResponse.success(it) }
            ?: PlacedCardDbResponse.notFoundErrorResponse
    }

    override suspend fun delete(request: PlacedCardIdDbRequest): PlacedCardDbResponse {
        return doUpdate(request.id, request.lock) { key, _ ->
            cache.invalidate(key)
            PlacedCardDbResponse.SUCCESS_EMPTY
        }
    }

    override suspend fun move(request: PlacedCardMoveDbRequest): PlacedCardDbResponse =
        doUpdate(request.id, request.lock) { key, storedValue ->
            val updated = storedValue.toInternal()
                .copy(
                    lock = FcPlacedCardLock(randomUuid()),
                    box = request.box,
                    updatedAt = Clock.System.now(),
                )
            cache.put(key, PlacedCardEntity(updated))
            PlacedCardDbResponse.success(updated)
        }


    override suspend fun select(request: PlacedCardSelectDbRequest): PlacedCardDbResponse {
        val ownerId = request.ownerId.asStringOrNull()
        val box = request.box.asStringOrNull()
        val timeSelector = when (request.strategy) {
            EARLIEST_CREATED -> getDateTimeSelector { it.createdAt }
            EARLIEST_REVIEWED, NONE -> getDateTimeSelector { it.updatedAt }
        }
        return cache.asMap().values.asSequence()
            .filter { box == null || it.box == box }
            .filter { ownerId == null || it.ownerId == ownerId }
            .minByOrNull(timeSelector)
            ?.toInternal()
            ?.let { PlacedCardDbResponse.success(it) }
            ?: PlacedCardDbResponse.notFoundErrorResponse
    }

    private fun getDateTimeSelector(getter: (PlacedCardEntity) -> String): (PlacedCardEntity) -> Instant =
        { it -> Instant.parse(getter(it)) }

    private fun save(card: PlacedCard) {
        val entity = PlacedCardEntity(card)
        if (entity.id == null) return
        cache.put(entity.id, entity)
    }

    private suspend fun doUpdate(
        id: PlacedCardId,
        lockBefore: FcPlacedCardLock,
        successBlock: (key: String, storedValue: PlacedCardEntity) -> PlacedCardDbResponse
    ): PlacedCardDbResponse {
        val key = id.asStringOrNull() ?: return PlacedCardDbResponse.emptyIdErrorResponse
        val lockBeforeStr = lockBefore.asStringOrNull() ?: return PlacedCardDbResponse.emptyLockErrorResponse
        return mutex.withLock {
            val storedValue = cache.get(key)
            when {
                storedValue == null -> PlacedCardDbResponse.notFoundErrorResponse
                storedValue.lock != lockBeforeStr ->
                    PlacedCardDbResponse.errorConcurrent(lockBefore, storedValue.toInternal())

                else -> successBlock(key, storedValue)
            }
        }
    }
}




