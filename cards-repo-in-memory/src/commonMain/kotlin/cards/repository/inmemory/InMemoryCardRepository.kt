package com.github.kondury.flashcards.cards.repository.inmemory

import com.benasher44.uuid.uuid4
import com.github.kondury.flashcards.cards.common.models.Card
import com.github.kondury.flashcards.cards.common.models.CardId
import com.github.kondury.flashcards.cards.common.models.FcError
import com.github.kondury.flashcards.cards.common.models.isNotEmpty
import com.github.kondury.flashcards.cards.common.repository.CardDbRequest
import com.github.kondury.flashcards.cards.common.repository.CardDbResponse
import com.github.kondury.flashcards.cards.common.repository.CardRepository
import com.github.kondury.flashcards.cards.repository.inmemory.model.CardEntity
import io.github.reactivecircus.cache4k.Cache
import com.github.kondury.flashcards.cards.common.repository.CardIdDbRequest
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

    init {
        initObjects.forEach {
            save(it)
        }
    }

    override suspend fun create(request: CardDbRequest): CardDbResponse {
        val key = randomUuid()
        val card = request.card.copy(
            id = CardId(key),
        )
        val entity = CardEntity(card)
        cache.put(key, entity)
        return CardDbResponse.success(card)
    }

    override suspend fun read(request: CardIdDbRequest): CardDbResponse {
        val key = request.id.asStringOrNull() ?: return emptyIdError
        return cache.get(key)
            ?.toInternal()
            ?.let { CardDbResponse.success(it) }
            ?: notFoundError
    }

    override suspend fun delete(request: CardIdDbRequest): CardDbResponse {
        val key = request.id.asStringOrNull() ?: return emptyIdError
        cache.invalidate(key)
        return CardDbResponse.SUCCESS_EMPTY
    }

    private fun save(card: Card) {
        val entity = CardEntity(card)
        if (entity.id == null) return
        cache.put(entity.id, entity)
    }

    companion object {
        val emptyIdError = CardDbResponse.error(
            FcError(
                code = "id-empty",
                group = "validation",
                field = "id",
                message = "Id must not be null or blank"
            )
        )
        val notFoundError = CardDbResponse.error(
            FcError(
                code = "not-found",
                field = "id",
                message = "Not Found"
            )
        )
    }

    private fun CardId.asStringOrNull() = this.takeIf { it.isNotEmpty() }?.asString()
}