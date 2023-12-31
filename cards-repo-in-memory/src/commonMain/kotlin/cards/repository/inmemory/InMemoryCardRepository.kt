package com.github.kondury.flashcards.cards.repository.inmemory

import com.benasher44.uuid.uuid4
import com.github.kondury.flashcards.cards.common.models.*
import com.github.kondury.flashcards.cards.common.repository.CardDbRequest
import com.github.kondury.flashcards.cards.common.repository.CardDbResponse
import com.github.kondury.flashcards.cards.common.repository.CardDbResponse.Companion.emptyIdErrorResponse
import com.github.kondury.flashcards.cards.common.repository.CardDbResponse.Companion.emptyLockErrorResponse
import com.github.kondury.flashcards.cards.common.repository.CardDbResponse.Companion.notFoundErrorResponse
import com.github.kondury.flashcards.cards.common.repository.CardIdDbRequest
import com.github.kondury.flashcards.cards.common.repository.CardRepository
import com.github.kondury.flashcards.cards.repository.inmemory.model.CardEntity
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class InMemoryCardRepository(
    initObjects: Collection<Card> = emptyList(),
    ttl: Duration = 2.minutes,
    val randomUuid: () -> String = { uuid4().toString() },
) : CardRepository {

    private val cache = Cache.Builder<String, CardEntity>()
        .expireAfterWrite(ttl)
        .build()
    private val mutex: Mutex = Mutex()

    init {
        initObjects.forEach {
            save(it)
        }
    }

    override suspend fun create(request: CardDbRequest): CardDbResponse {
        val key = randomUuid()
        val card = request.card.copy(
            id = CardId(key),
            lock = FcCardLock(randomUuid())
        )
        val entity = CardEntity(card)
        cache.put(key, entity)
        return CardDbResponse.success(card)
    }

    override suspend fun read(request: CardIdDbRequest): CardDbResponse {
        val key = request.id.asStringOrNull() ?: return emptyIdErrorResponse
        return cache.get(key)
            ?.toInternal()
            ?.let { CardDbResponse.success(it) }
            ?: notFoundErrorResponse
    }

    override suspend fun delete(request: CardIdDbRequest): CardDbResponse {
        return doUpdate(request.id, request.lock) { key, _ ->
            cache.invalidate(key)
            CardDbResponse.SUCCESS_EMPTY
        }
    }

    private fun save(card: Card) {
        val entity = CardEntity(card)
        if (entity.id == null) return
        cache.put(entity.id, entity)
    }

    private suspend fun doUpdate(
        id: CardId,
        lockBefore: FcCardLock,
        successBlock: (key: String, oldCard: CardEntity) -> CardDbResponse
    ): CardDbResponse {
        val key = id.asStringOrNull() ?: return emptyIdErrorResponse
        val lockBeforeStr = lockBefore.asStringOrNull() ?: return emptyLockErrorResponse
        return mutex.withLock {
            val storedCard = cache.get(key)
            when {
                storedCard == null -> notFoundErrorResponse
                storedCard.lock != lockBeforeStr ->
                    CardDbResponse.errorConcurrent(
                        lockBefore,
                        storedCard.toInternal()
                    )
                else -> successBlock(key, storedCard)
            }
        }
    }

    private fun CardId.asStringOrNull() = this.takeIf { it.isNotEmpty() }?.asString()
    private fun FcCardLock.asStringOrNull() = this.takeIf { it.isNotEmpty() }?.asString()
}