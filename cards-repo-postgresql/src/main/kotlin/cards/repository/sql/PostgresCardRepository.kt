package com.github.kondury.flashcards.cards.repository.sql

import com.benasher44.uuid.uuid4
import com.github.kondury.flashcards.cards.common.helpers.asFcError
import com.github.kondury.flashcards.cards.common.models.Card
import com.github.kondury.flashcards.cards.common.models.CardId
import com.github.kondury.flashcards.cards.common.models.FcCardLock
import com.github.kondury.flashcards.cards.common.models.isEmpty
import com.github.kondury.flashcards.cards.common.repository.CardDbRequest
import com.github.kondury.flashcards.cards.common.repository.CardDbResponse
import com.github.kondury.flashcards.cards.common.repository.CardIdDbRequest
import com.github.kondury.flashcards.cards.common.repository.CardRepository
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction

class PostgresCardRepository(
    properties: SqlProperties,
    initObjects: Collection<Card> = emptyList(),
    val randomUuid: () -> String = { uuid4().toString() },
) : CardRepository {
    private val cardTable = CardTable(properties.table)

    private val driver = when {
        properties.url.startsWith("jdbc:postgresql://") -> "org.postgresql.Driver"
        else -> throw IllegalArgumentException("Unknown driver for url ${properties.url}")
    }

    private val connection = Database.connect(
        properties.url, driver, properties.user, properties.password
    )

    init {
        transaction(connection) {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(cardTable)
            initObjects.forEach { create(it) }
        }
    }

    override suspend fun create(request: CardDbRequest): CardDbResponse =
        transactionWrapper { create(request.card) }

    override suspend fun read(request: CardIdDbRequest): CardDbResponse =
        transactionWrapper { getById(request.id) }

    override suspend fun delete(request: CardIdDbRequest): CardDbResponse =
        transactionWrapper {
            updateOptimistic(request.id, request.lock) {
                cardTable.deleteWhere { cardTable.id eq request.id.asString() }
                CardDbResponse.SUCCESS_EMPTY
            }
        }

    private fun transactionWrapper(block: () -> CardDbResponse): CardDbResponse =
        try {
            transaction(connection) {
                addLogger(StdOutSqlLogger)
                block()
            }
        } catch (e: Exception) {
            CardDbResponse.error(e.asFcError())
        }

    private fun create(requestCard: Card): CardDbResponse =
        cardTable.insert { toRow(it, requestCard, randomUuid) }.resultedValues
            ?.mapSingleOrNull { CardDbResponse.success(it) }
            ?: throw RuntimeException("DB error: insert statement returned empty result")

    private fun updateOptimistic(
        id: CardId,
        lock: FcCardLock,
        updateBlock: (Card) -> CardDbResponse
    ): CardDbResponse =
        getById(id) {
            if (it.lock != lock) CardDbResponse.errorConcurrent(lock, it)
            else updateBlock(it)
        }

    private fun getById(
        id: CardId,
        postProcess: (Card) -> CardDbResponse = { CardDbResponse.success(it) }
    ): CardDbResponse =
        if (id.isEmpty()) CardDbResponse.emptyIdErrorResponse
        else {
            cardTable.select { cardTable.id eq id.asString() }
                .mapSingleOrNull { postProcess(it) }
                ?: CardDbResponse.notFoundErrorResponse
        }

    private fun Iterable<ResultRow>.mapSingleOrNull(
        postProcess: (Card) -> CardDbResponse
    ): CardDbResponse? =
        this.singleOrNull()
            ?.let { cardTable.fromRow(it) }
            ?.let { postProcess(it) }
}